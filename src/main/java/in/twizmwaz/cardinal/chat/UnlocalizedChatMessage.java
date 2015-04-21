package in.twizmwaz.cardinal.chat;

import java.util.Locale;

public class UnlocalizedChatMessage implements ChatMessage  {
    
    private final String message;
    private ChatMessage[] messages = {};
    private String[] strings = {};

    public UnlocalizedChatMessage(String message) {
        this.message = message;
    }

    public UnlocalizedChatMessage(String message, ChatMessage... messages) {
        this.message = message;
        this.messages = messages;
    }

    public UnlocalizedChatMessage(String message, String... strings) {
        this.message = message;
        this.strings = strings;
    }

    @Override
    public String getMessage(String locale) {
        String message = this.message;
        for (int i = 0; i < this.messages.length; i++) {
            message = message.replaceAll("\\{" + i + "\\}", this.messages[i].getMessage(locale));
        }
        for (int i = 0; i < this.strings.length; i++) {
            message = message.replaceAll("\\{" + i + "\\}", this.strings[i]);
        }
        return message;
    }

    @Override
    public String toString() {
        return getMessage(Locale.getDefault().toString());
    }

}
