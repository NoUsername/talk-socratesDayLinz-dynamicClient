
# Dynamic UI Client with JavaScript logic

Presentation that shows how a dynamic UI client could be built that offers
scriptable behaviour via JavaScript.

The JavaScript is executed using Java8 Nashorn engine.

It even shows that this code is testable by running the same scripts
inside some JUnit tests.

e.g. comment out this line: `ui.setFocus('age');` from the `test.response` file
and run `gradlew build` to see that the tests start failing.

To run the example:

Run the server:
`at.paukl.dynclient.server.Main`

Then run the client:
`at.paukl.dynclient.client.Client`


## Context

This was a short demo I showed at the SoCraTes Day Linz 2016-10-29.

Just a minmal example of how generic UIs could be built that use JavaScript for their logic.
The demo was simply intended to start a discussion of how this could be used to e.g. also
add some server-dictated logic to Android/iOS/... other native applications since many
platforms have included JavaScript engines.
