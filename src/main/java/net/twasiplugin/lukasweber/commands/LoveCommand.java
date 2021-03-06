package net.twasiplugin.lukasweber.commands;

import net.twasi.core.database.models.Language;
import net.twasi.core.database.models.User;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.customcommands.TwasiCustomCommandEvent;
import net.twasi.core.plugin.api.customcommands.TwasiPluginCommand;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.translations.renderer.TranslationRenderer;
import net.twasi.twitchapi.helix.users.response.UserDTO;
import net.twasi.twitchapi.options.TwitchRequestOptions;
import net.twasiplugin.lukasweber.database.LoveEntity;
import net.twasiplugin.lukasweber.database.LoveRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.twasi.twitchapi.TwitchAPI.helix;

public class LoveCommand extends TwasiPluginCommand {

    private LoveRepository repo = ServiceRegistry.get(DataService.class).get(LoveRepository.class);

    public LoveCommand(TwasiUserPlugin twasiUserPlugin) {
        super(twasiUserPlugin);
    }

    private List<String> cryEmotes = Arrays.asList(":(", "BibleThump", "WutFace", "DansGame", "FailFish", "NotLikeThis", "ScaredyCat", "ResidentSleeper", "ANELE", "RuleFive", "SabaPing", "AngryJack", "SMOrc");
    private List<String> normalEmotes = Arrays.asList(":)", "TheIlluminati", "YouDontSay", "SabaPing", "HappyJack", "BrokeBack", "KevinTurtle", "ThunBeast", "CoolStoryBob", "PartyPopper", "<3");
    private List<String> loveEmotes = Arrays.asList("Kreygasm", "PogChamp", "TableHere <3", "FortHype", "ThunBeast", "HassaanChop", "bleedPurple", "SeemsGood", "MVGame", "Poooound", "<3 <3 <3");

    @Override
    protected boolean execute(TwasiCustomCommandEvent e) {
        TranslationRenderer renderer = e.getRenderer();
        User user = e.getStreamer().getUser();
        if (!e.hasArgs()) {
            e.reply(renderer.render("syntax"));
            return false;
        }
        UserDTO resolvedUser;
        try {
            resolvedUser = helix().users().getUsers(null, new String[]{e.getArgs().get(0)}, new TwitchRequestOptions().withAuth(user.getTwitchAccount().toAuthContext())).get(0);
        } catch (Exception ex) {
            e.reply(renderer.render("love.error.notfound"));
            return false;
        }
        LoveEntity byTwitchIds = repo.getByTwitchIds(user, e.getSender().getTwitchId(), resolvedUser.getId());
        renderer.bindObject("entity", byTwitchIds);
        float num = byTwitchIds.getNumber() * 1f;
        renderer.bind("cloud", Math.round(num / 100f * 9f) + "");
        if (user.getConfig().getLanguage().equals(Language.DE_DE))
            renderer.bind("cloud", Math.round(byTwitchIds.getNumber() / 100f * 7f) + "");
        Collections.shuffle(cryEmotes);
        Collections.shuffle(normalEmotes);
        Collections.shuffle(loveEmotes);
        String emote = cryEmotes.get(0);
        if (byTwitchIds.getNumber() >= 20) emote = normalEmotes.get(0);
        if (byTwitchIds.getNumber() >= 70) emote = loveEmotes.get(0);
        renderer.bind("emote", emote);
        e.reply(renderer.render(byTwitchIds.getLoveAnswer().toString()));
        return true;
    }

    public String getCommandName() {
        return "love";
    }
}
