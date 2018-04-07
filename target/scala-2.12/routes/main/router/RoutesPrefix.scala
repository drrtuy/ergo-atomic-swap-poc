// @GENERATOR:play-routes-compiler
// @SOURCE:/git/play-scala-forms-example/conf/routes
// @DATE:Sat Apr 07 14:48:30 UTC 2018


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
