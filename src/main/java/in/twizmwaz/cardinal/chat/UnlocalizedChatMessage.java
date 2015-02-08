package in.twizmwaz.cardinal.chat;

public class UnlocalizedChatMessage implements ChatMessage  {
    
    private final String message;
    
    public UnlocalizedChatMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage(String locale, ChatMessage... messages) {
        String message = this.message;
        for (int i = 0; i < messages.length; i++) {
            message = message.replaceAll("\\{" + i + "\\}", messages[i].getMessage(locale));
        }
        return message;
    }

}
