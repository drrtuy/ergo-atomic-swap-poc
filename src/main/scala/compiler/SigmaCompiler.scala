package compiler {

  import sigmastate._
  import sigmastate.Values._
  import sigmastate.lang.{SigmaBinder, SigmaParser, SigmaSpecializer, SigmaTyper}
  import sigmastate.utxo._
  import scorex.crypto.hash.Blake2b256
  import compiler.BoxHelpers.{fakeMessage, fakeSelf}
  import sigmastate.lang.Terms._
  import java.util.Base64
  //import java.util.Base64.getDecoder
  //import java.util.Base64.getEncoder


  object UpperComp extends App {

    val compiler = new SigmaCompiler()
    
    //compiler.sample()
    compiler.swap()

  }

  class SigmaCompiler {

    def sample(): Unit = {

      println("Starting to compile...")

      val verifier = new ErgoInterpreter

      //backer's prover with his private key
      val backerProver = new ErgoProvingInterpreter
      //project's prover with his private key
      val projectProver = new ErgoProvingInterpreter
      val backerPubKey = backerProver.dlogSecrets.head.publicImage
      val projectPubKey = projectProver.dlogSecrets.head.publicImage

      val timeout = IntConstant(100)
      val minToRaise = IntConstant(1000)

      val env = Map(
        "timeout" -> 100,
        "minToRaise" -> 1000,
        "backerPubKey" -> backerPubKey,
        "projectPubKey" -> projectPubKey
      )

      val compiledScript = compile(env,
        """{
          | let c1 = HEIGHT >= timeout && backerPubKey
          | let c2 = allOf(Array(
          |   HEIGHT < timeout,
          |   projectPubKey,
          |   OUTPUTS.exists(fun (out: Box) = {
          |     out.value >= minToRaise && out.propositionBytes == projectPubKey.propBytes
          |   })
          | ))
          | c1 || c2
          | }
        """.stripMargin).asBoolValue

      println("Simpe output")
      println("{")
      println("\t%s".format(compiledScript))
      println("\topCode: %s".format(compiledScript.opCode))
      println("\tpropBytes: %s".format(compiledScript.propBytes))
      println("\tcost: %s".format(compiledScript.cost))
      println("\tevaluated: %s".format(compiledScript.evaluated))
      println("}")

      val tx1Output1 = ErgoBox(minToRaise.value, projectPubKey)
      val tx1Output2 = ErgoBox(1, projectPubKey)

      val tx1 = ErgoTransaction(IndexedSeq(), IndexedSeq(tx1Output1, tx1Output2))

      val crowdFundingScript = OR(
        AND(GE(Height, timeout), backerPubKey),
        AND(
          Seq(
            LT(Height, timeout),
            projectPubKey,
            Exists(Outputs, 21,
              AND(
                GE(ExtractAmount(TaggedBox(21)), minToRaise),
                EQ(ExtractScriptBytes(TaggedBox(21)), ByteArrayConstant(projectPubKey.propBytes))
              )
            )
          )
        )
      )



      val outputToSpend = ErgoBox(10, crowdFundingScript)

      val ctx1 = ErgoContext(
        currentHeight = timeout.value,
        lastBlockUtxoRoot = AvlTreeData.dummy,
        boxesToSpend = IndexedSeq(),
        spendingTransaction = tx1,
        self = outputToSpend)

      val result = backerProver.prove(compiledScript, ctx1, fakeMessage)
      println("Prove result: %s".format(result))
    }

    def swap(): Unit = {
      println("Atomic swap check starting")
      val proverA = new ErgoProvingInterpreter
      val proverB = new ErgoProvingInterpreter
      val pubkeyA = proverA.dlogSecrets.head.publicImage
      val pubkeyB = proverB.dlogSecrets.head.publicImage
      val verifier = new ErgoInterpreter

      val x = proverA.contextExtenders(1).value.asInstanceOf[Array[Byte]]
      //val xB64 = Base64.getEncoder.encodeToString(x)
      println("x - прообраз значения, используемого в операции swap-а: { %s }".format(x))
      //println(xB64)
      val hx = ByteArrayConstant(Blake2b256(x))
      println("Blake2b256 хэш от x: [%s]".format(hx))
      //val hxB64 = Base64.getEncoder.encodeToString(hx)
      //println(hxB64)

      val height1 = 100000
      val height2 = 50000

      val deadlineA = 1000
      val deadlineB = 500

      println("Номер блока перед началом swap-а в chain1: [%s]".format(height1))
      println("Номер блока пере началом swap-а в chain2: [%s]".format(height2))
      println("Таймаут для операции swap-а для Bob: [%s]".format(deadlineA))
      println("Таймаут для операции swap-а для Alice: [%s]".format(deadlineB))

      scala.io.StdIn.readLine()
      val env = Map(
        "height1" -> height1, "height2" -> height2,
        "deadlineA" -> deadlineA, "deadlineB" -> deadlineB,
        "pubkeyA" -> pubkeyA, "pubkeyB" -> pubkeyB, "hx" -> hx)
      val prop1 = compile(env,
        """{
          |  anyOf(Array(
          |    HEIGHT > height1 + deadlineA && pubkeyA,
          |    pubkeyB && blake2b256(taggedByteArray(1)) == hx
          |  ))
          |}""".stripMargin).asBoolValue

      //chain1 script
      val prop1Tree = OR(
        AND(GT(Height, Plus(IntConstant(height1), IntConstant(deadlineA))), pubkeyA),
        AND(pubkeyB, EQ(CalcBlake2b256(TaggedByteArray(1)), hx))
      )
      //prop1 shouldBe prop1Tree
      //println("\t%s".format(prop1))

      val prop2 = compile(env,
        """{
          |  anyOf(Array(
          |    HEIGHT > height2 + deadlineB && pubkeyB,
          |    pubkeyA && blake2b256(taggedByteArray(1)) == hx
          |  ))
          |}
        """.stripMargin).asBoolValue

      //chain2 script
      val prop2Tree = OR(
        AND(GT(Height, Plus(IntConstant(height2), IntConstant(deadlineB))), pubkeyB),
        AND(pubkeyA, EQ(CalcBlake2b256(TaggedByteArray(1)), hx))
      )
      //prop2 shouldBe prop2Tree

      //Preliminary checks:

      println("Предварительные проверки сразу после транзакции Alice.")
      //B can't spend coins of A in chain1 (generate a valid proof)
      val ctxf1 = ErgoContext(
        currentHeight = height1 + 1,
        lastBlockUtxoRoot = AvlTreeData.dummy,
        boxesToSpend = IndexedSeq(),
        spendingTransaction = null,
        self = fakeSelf)

      val prBtest1 = proverB.prove(prop1, ctxf1, fakeMessage).isSuccess
      println("Bob пытается увести коины Alice без верного образа X. CUR_HEIGHT = start_height1 + 1 и в контексте нет секрета X")
      scala.io.StdIn.readLine()
      println("Результат проверки: %s".format(prBtest1))
      scala.io.StdIn.readLine()

      //A can't withdraw her coins in chain1 (generate a valid proof)
      val prAtest2 = proverA.prove(prop1, ctxf1, fakeMessage).isSuccess
      println("Alice пытается вывести коины в chain1 до наступления TO, т.е. CUR_HEIGHT < start_height1 + deadline1.")
      scala.io.StdIn.readLine()
      println("Результат проверки: %s".format(prAtest2))
      scala.io.StdIn.readLine()

      //B cant't withdraw his coins in chain2 (generate a valid proof)
      val ctxf2 = ErgoContext(
        currentHeight = height2 + 1,
        lastBlockUtxoRoot = AvlTreeData.dummy,
        boxesToSpend = IndexedSeq(), spendingTransaction = null, self = fakeSelf)
      val prBtest3 = proverB.prove(prop2, ctxf2, fakeMessage).isSuccess
      println("Bob пытается вывести коина до наступления TO в chain2, т.е. CUR_HEIGHT < start_height2 + deadline2.")
      scala.io.StdIn.readLine()
      println("Результат проверки: %s".format(prBtest3))

      //Successful run below:

      //A spends coins of B in chain2
      val ctx1 = ErgoContext(
        currentHeight = height2 + 1,
        lastBlockUtxoRoot = AvlTreeData.dummy,
        boxesToSpend = IndexedSeq(),
        spendingTransaction = null,
        self = fakeSelf)
      val pr = proverA.prove(prop2, ctx1, fakeMessage).get
      println("Alice использует коины в chain2, оставляя в blockchain образ x.")
      scala.io.StdIn.readLine()
      //verifier.verify(prop2, ctx1, pr, fakeMessage).get shouldBe true

      //B extracts preimage x of hx
      println("Bob получает образ x и...")
      scala.io.StdIn.readLine()
      val t = pr.extension.values(1)
        val proverB2 = proverB.withContextExtender(1, t.asInstanceOf[ByteArrayConstant])

      println("...использует его в своих грязных целях в chain1.")

      //B spends coins of A in chain1 with knowledge of x
      val ctx2 = ErgoContext(
        currentHeight = height1 + 1,
        lastBlockUtxoRoot = AvlTreeData.dummy,
        boxesToSpend = IndexedSeq(),
        spendingTransaction = null,
        self = fakeSelf)
      val pr2 = proverB2.prove(prop1, ctx2, fakeMessage).get
      val BspendAresult = verifier.verify(prop1, ctx2, pr2, fakeMessage).get
      println("Результат проверки: %s".format(BspendAresult))
      scala.io.StdIn.readLine()
    }

    def compile(env: Map[String, Any], code: String): Value[SType] = {
      val parsed = parse(code)
      val binder = new SigmaBinder(env)
      val bound = binder.bind(parsed)
      val st = new SigmaTree(bound)
      val typer = new SigmaTyper
      val typed = typer.typecheck(bound)
      val spec = new SigmaSpecializer
      val ir = spec.specialize(typed)
      ir
    }

    def parse(x: String): SValue = SigmaParser(x).get.value
  }

}