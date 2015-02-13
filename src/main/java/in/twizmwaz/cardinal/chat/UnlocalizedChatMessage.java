package in.twizmwaz.cardinal.chat;

public class UnlocalizedChatMessage implements ChatMessage  {
    
    private final String message;
    private final ChatMessage[] strings;
    
    public UnlocalizedChatMessage(String message, ChatMessage... strings) {
        this.message = message;
        this.strings = strings;
    }

    @Override
    public String getMessage(String locale) {
        String message = this.message;
        for (int i = 0; i < this.strings.length; i++) {
            message = message.replaceAll("\\{" + i + "\\}", this.strings[i].getMessage(locale));
        }
        return message;
    }

}
