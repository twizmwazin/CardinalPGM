package in.twizmwaz.cardinal.chat;

public class LocalizedChatMessage implements ChatMessage {
    
    private final ChatConstant message;
    private final ChatMessage[] strings;

    public LocalizedChatMessage(ChatConstant message, ChatMessage... strings) {
        this.message = message;
        this.strings = strings;
    }

    @Override
    public String getMessage(String locale) {
        String message = this.message.getMessage(locale);
        for (int i = 0; i < this.strings.length; i++) {
            message = message.replaceAll("\\{" + i + "\\}", this.strings[i].getMessage(locale));
        }
        return message;
    }
}
