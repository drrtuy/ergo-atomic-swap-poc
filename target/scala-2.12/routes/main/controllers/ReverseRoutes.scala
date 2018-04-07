// @GENERATOR:play-routes-compiler
// @SOURCE:/git/play-scala-forms-example/conf/routes
// @DATE:Sat Apr 07 14:48:30 UTC 2018

import play.api.mvc.Call


import _root_.controllers.Assets.Asset

// @LINE:6
package controllers {

  // @LINE:17
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:17
    def versioned(file:Asset): Call = {
      implicit lazy val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public"))); _rrc
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
    }
  
  }

  // @LINE:6
  class ReverseWidgetController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:10
    def createWidget(): Call = {
    
      () match {
      
        // @LINE:10
        case ()  =>
          
          Call("POST", _prefix + { _defaultPrefix } + "dbg")
      
      }
    
    }
  
    // @LINE:6
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
    // @LINE:9
    def listWidgets(): Call = {
    
      () match {
      
        // @LINE:9
        case ()  =>
          
          Call("GET", _prefix + { _defaultPrefix } + "dbg")
      
      }
    
    }
  
  }


}
