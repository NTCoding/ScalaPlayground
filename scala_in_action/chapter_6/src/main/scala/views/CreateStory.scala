package views

object CreateStory {

    def apply(message: String = "") =
        <html>
            <head>
                <title>Create new Story</title>
                <link rel="stylesheet" href="css/main.css" type="text/css" media="screen" charset="utf-8" />
            </head>
            <body>
                <span class="message">{message}</span>
                <div class="createStory">
                    <form action="/card/save" method="post" accept-charset="utf-8">
                        <fieldset>
                            <legend>Create a new Story</legend>
                            <div class="section">
                                <label for="storyNumber">
                                    Story Number
                                    <span class="subtle">
                                        (uniquely identifies a story)
                                    </span>
                                </label>
                            </div>
                        </fieldset>
                    </form>
                </div>
            </body>
        </html>

}
