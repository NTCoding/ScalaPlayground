import scalaz._
import Scalaz._
import scalaz.http._
import response._
import request._
import servlet._
import HttpServlet._
import Slinky._


final class WeKanbanApplication extends StreamStreamServletApplication {

  val application = new ServletApplication[Stream, Stream] {
      def application(implicit servlet: HttpServlet, servletRequest: HttpServletRequest, request: Request[Stream]) = {
        HttpServlet.resource(x => OK << x.toStream, NotFound.xhtml)
      }
  }

}
