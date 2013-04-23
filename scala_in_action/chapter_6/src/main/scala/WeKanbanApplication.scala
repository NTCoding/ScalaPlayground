import scalaz._
import Scalaz._
import scalaz.http._
import scalaz.http.request.Request.MethodParts
import servlet._
import HttpServlet.resource
import Slinky._
import views.CreateStory


final class WeKanbanApplication extends StreamStreamServletApplication {
    import response._
    import request._
    implicit val charset = UTF8

  val application = new ServletApplication[Stream, Stream] {

      def application(implicit servlet: HttpServlet, servletRequest: HttpServletRequest, request: Request[Stream]) = {
          def found(x: Iterator[Byte]): Response[Stream] = OK << x.toStream
          handle | HttpServlet.resource(found, NotFound.xhtml)
      }

      def param(name: String)(implicit request: Request[Stream]) = (request ! name).getOrElse(List[Char]()).mkString("")

      def handle(implicit request: Request[Stream], servletRequest: HttpServletRequest): Option[Response[Stream]] = {
          request match {
              case MethodParts(GET, "card" :: "create" :: Nil) =>
                  Some(OK(ContentType, "text/html") << strict << CreateStory(param("message")))

              case _ => None
          }
      }

  }

}
