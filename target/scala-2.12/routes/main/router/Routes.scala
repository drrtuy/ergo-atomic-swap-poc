// @GENERATOR:play-routes-compiler
// @SOURCE:/git/play-scala-forms-example/conf/routes
// @DATE:Sat Apr 07 14:48:30 UTC 2018

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:6
  WidgetController_1: controllers.WidgetController,
  // @LINE:17
  Assets_0: controllers.Assets,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:6
    WidgetController_1: controllers.WidgetController,
    // @LINE:17
    Assets_0: controllers.Assets
  ) = this(errorHandler, WidgetController_1, Assets_0, "/")

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, WidgetController_1, Assets_0, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """controllers.WidgetController.index"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """dbg""", """controllers.WidgetController.listWidgets"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """dbg""", """controllers.WidgetController.createWidget"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """widgets""", """controllers.WidgetController.listWidgets"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """widgets""", """controllers.WidgetController.createWidget"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(path:String = "/public", file:Asset)"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:6
  private[this] lazy val controllers_WidgetController_index0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_WidgetController_index0_invoker = createInvoker(
    WidgetController_1.index,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.WidgetController",
      "index",
      Nil,
      "GET",
      this.prefix + """""",
      """ Home page""",
      Seq()
    )
  )

  // @LINE:9
  private[this] lazy val controllers_WidgetController_listWidgets1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("dbg")))
  )
  private[this] lazy val controllers_WidgetController_listWidgets1_invoker = createInvoker(
    WidgetController_1.listWidgets,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.WidgetController",
      "listWidgets",
      Nil,
      "GET",
      this.prefix + """dbg""",
      """ Debugger""",
      Seq()
    )
  )

  // @LINE:10
  private[this] lazy val controllers_WidgetController_createWidget2_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("dbg")))
  )
  private[this] lazy val controllers_WidgetController_createWidget2_invoker = createInvoker(
    WidgetController_1.createWidget,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.WidgetController",
      "createWidget",
      Nil,
      "POST",
      this.prefix + """dbg""",
      """""",
      Seq()
    )
  )

  // @LINE:13
  private[this] lazy val controllers_WidgetController_listWidgets3_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("widgets")))
  )
  private[this] lazy val controllers_WidgetController_listWidgets3_invoker = createInvoker(
    WidgetController_1.listWidgets,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.WidgetController",
      "listWidgets",
      Nil,
      "GET",
      this.prefix + """widgets""",
      """ Widgets""",
      Seq()
    )
  )

  // @LINE:14
  private[this] lazy val controllers_WidgetController_createWidget4_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("widgets")))
  )
  private[this] lazy val controllers_WidgetController_createWidget4_invoker = createInvoker(
    WidgetController_1.createWidget,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.WidgetController",
      "createWidget",
      Nil,
      "POST",
      this.prefix + """widgets""",
      """""",
      Seq()
    )
  )

  // @LINE:17
  private[this] lazy val controllers_Assets_versioned5_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned5_invoker = createInvoker(
    Assets_0.versioned(fakeValue[String], fakeValue[Asset]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String], classOf[Asset]),
      "GET",
      this.prefix + """assets/""" + "$" + """file<.+>""",
      """ Map static resources from the /public folder to the /assets URL path""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:6
    case controllers_WidgetController_index0_route(params@_) =>
      call { 
        controllers_WidgetController_index0_invoker.call(WidgetController_1.index)
      }
  
    // @LINE:9
    case controllers_WidgetController_listWidgets1_route(params@_) =>
      call { 
        controllers_WidgetController_listWidgets1_invoker.call(WidgetController_1.listWidgets)
      }
  
    // @LINE:10
    case controllers_WidgetController_createWidget2_route(params@_) =>
      call { 
        controllers_WidgetController_createWidget2_invoker.call(WidgetController_1.createWidget)
      }
  
    // @LINE:13
    case controllers_WidgetController_listWidgets3_route(params@_) =>
      call { 
        controllers_WidgetController_listWidgets3_invoker.call(WidgetController_1.listWidgets)
      }
  
    // @LINE:14
    case controllers_WidgetController_createWidget4_route(params@_) =>
      call { 
        controllers_WidgetController_createWidget4_invoker.call(WidgetController_1.createWidget)
      }
  
    // @LINE:17
    case controllers_Assets_versioned5_route(params@_) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned5_invoker.call(Assets_0.versioned(path, file))
      }
  }
}
