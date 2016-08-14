package flashcards;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This simple sample has no external dependencies or session management, and shows the most basic
 * example of how to handle Alexa Skill requests.
 */
class FlashcardSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(FlashcardSpeechlet.class);
    private static final String CARD_TYPE_SLOT = "CardType";

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        String speechOutput = "Which flash cards would you like to try?  The current options are: multiplication tables or multiplication tables.";
        // If the user either does not reply to the welcome message or says
        // something that is not understood, they will be prompted again with this text.
        String repromptText = "For instructions on what you can say, please say help me.";

        return newAskResponse(speechOutput, repromptText);
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if ("CardTypeIntent".equals(intentName)) {
            return getCardTypeResponse(intent);

        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();

        } else if ("AMAZON.StopIntent".equals(intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");

            return SpeechletResponse.newTellResponse(outputSpeech);
        } else if ("AMAZON.CancelIntent".equals(intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");

            return SpeechletResponse.newTellResponse(outputSpeech);
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }

    private SpeechletResponse getCardTypeResponse(Intent cardType) {
        Slot cardTypeSlot = cardType.getSlot(CARD_TYPE_SLOT);

        if (cardTypeSlot != null && cardTypeSlot.getValue() != null) {
            String deckName = cardTypeSlot.getValue();

            //Check if this deck exists
            if(deckName.equals("multiplication tables")) {
                // Create speech output
                String speechText = "Here I will ask you a question.";

                // Create the Simple card content.
                SimpleCard card = new SimpleCard();
                card.setTitle("Response");
                card.setContent(speechText);

                // Create the plain text output.
                PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
                speech.setText(speechText);

                return SpeechletResponse.newTellResponse(speech, card);
            } else {
                // We don't have this deck, so keep the session open and ask the user for another
                // item.
                String speechOutput =
                        "I'm sorry, I currently do not have a deck for " + deckName
                                + ". Tell me a different deck name.";
                String repromptSpeech = "Tell me a different deck name.";
                return newAskResponse(speechOutput, repromptSpeech);
            }
        } else {
            return getHelpResponse();
        }
    }

    /**
     * Returns a response for the help intent.
     */
    private SpeechletResponse getHelpResponse() {
        String speechText =
                "Say: multiplication tables or, cancel";

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt);
    }

    private SpeechletResponse getEmptyResponse(){
        String speechText =
                "Forget it, asshole.";

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt);
    }

    private String checkAnswer(String anserText){
        return "";
    }

    /**
     * Wrapper for creating the Ask response. The OutputSpeech and {@link Reprompt} objects are
     * created from the input strings.
     *
     * @param stringOutput
     *            the output to be spoken
     * @param repromptText
     *            the reprompt for if the user doesn't reply or is misunderstood.
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse newAskResponse(String stringOutput, String repromptText) {
        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText(stringOutput);

        PlainTextOutputSpeech repromptOutputSpeech = new PlainTextOutputSpeech();
        repromptOutputSpeech.setText(repromptText);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptOutputSpeech);

        return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    }

}
