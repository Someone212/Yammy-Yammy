package discbot;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.v3.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.attribute.IGuildChannelContainer;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.JDAImpl;

import java.io.IOException;
import java.util.List;


public class DiscordBot extends ListenerAdapter {
    public static List<TextChannel> translateChannel;
    public static void main(String[] args) throws IOException {
        JDA bot= (JDA) JDABuilder.createDefault("MTA2MDgwNTAyNzYyNDY1Njk2Ng.GHhVr8.kudKrOcBojhH2jai5mpIH6_-2uM8y4mOmoXh_k")
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.listening("to ur mom"))
                .addEventListeners(new DiscordBot())
                .build();
        translateChannel=bot.getTextChannels();
  }



    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(!event.getAuthor().isBot()) {
            String temp = event.getMessage().getContentDisplay();
            String translated;
            String language;
            Channel bruh=event.getChannel();
            String name=event.getAuthor().getName();
            translateChannel=event.getGuild().getTextChannelsByName("Translate",true);
            try {
                language=detectLanguage(temp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(language.equals("ko")){
                try {
                    translated = translateText(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                translateChannel.get(0).sendMessage(name+" "+translated).queue();

            }
            if(event.getAuthor().getId().equals("480059582744494080")&&event.getMessage().getContentDisplay().toLowerCase().contains("sion")){
                event.getMessage().delete().queue();
                event.getChannel().sendMessage("fuck you neel").queue();
            }
            if(event.getAuthor().getId().equals("480059582744494080")){
                int random=(int)(Math.random()*20);
                if(random==13){
                    event.getChannel().sendMessage("shut up neel").queue();
                }
            }
            if(event.getAuthor().getId().equals("410037926240583680")&&event.getMessage().getContentDisplay().toLowerCase().contains("cringe")){
                event.getChannel().sendMessage("https://tenor.com/view/dies-of-cringe-cringe-gif-20747133").queue();
            }
            if(event.getMessage().getContentDisplay().contains("noor")){
                event.getChannel().sendMessage("Holy shit noor? Like THE MASTERS EKKO PLAYER NOOR???? NO FUCKING WAY").queue();
            }
        }

    }



    public static String translateText(MessageReceivedEvent event) throws IOException {
        String projectId = "excellent-camp-373906";
        String targetLanguage = "en";
        String text = event.getMessage().getContentDisplay();
        return translateText(projectId, targetLanguage, text);
    }
    public static String translateText(String projectId, String targetLanguage, String text)
            throws IOException {
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            LocationName parent = LocationName.of(projectId, "global");
            TranslateTextRequest request =
                    TranslateTextRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setTargetLanguageCode(targetLanguage)
                            .addContents(text)
                            .build();
            TranslateTextResponse response = client.translateText(request);
            for (Translation translation : response.getTranslationsList()) {
                return translation.getTranslatedText();
            }
        }
        return "idk";
    }
    public static String detectLanguage(String originalMessage) throws IOException {
        String projectId = "excellent-camp-373906";
        String text = originalMessage;
        return detectLanguage(projectId, text);
    }
    public static String detectLanguage(String projectId, String text) throws IOException {
        if(text==""){
            return "en";
        }
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            LocationName parent = LocationName.of(projectId, "global");
            DetectLanguageRequest request =
                    DetectLanguageRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setContent(text)
                            .build();
            DetectLanguageResponse response = client.detectLanguage(request);
            for (DetectedLanguage language : response.getLanguagesList()) {
                return language.getLanguageCode();

            }
        }
        return "idk";
    }
}

