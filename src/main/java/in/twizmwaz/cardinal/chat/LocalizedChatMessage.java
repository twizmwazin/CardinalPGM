package in.twizmwaz.cardinal.chat;

public class LocalizedChatMessage implements ChatMessage {
    
    private final ChatConstant message;
    
    public LocalizedChatMessage(ChatConstant message) {
        this.message = message;
    }

    @Override
    public String getMessage(String locale, ChatMessage... messages) {
        String message = this.message.getMessage(locale);
        for (int i = 0; i < messages.length; i++) {
            message = message.replaceAll("\\{" + i + "\\}", messages[i].getMessage(locale));
        }
        return message;
    }
}
