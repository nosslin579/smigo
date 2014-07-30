package kga;

import kga.rules.CompanionRule;
import kga.rules.RepetitionRule;
import kga.rules.Rule;
import kga.rules.RuleType;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;

public class GardenTest {

    public static final String HINT_MESSAGE_KEY = "hint.hint";
    private Family carrotFamily = new Family(1, "CarrotFamily");
    private Species carrot = new Species(1, new HashSet<Rule>(), carrotFamily, true, false);
    private Species onion = new Species(2, new HashSet<Rule>(), carrotFamily, true, false);
    private Rule twoBrother = new CompanionRule(1, carrot, RuleType.goodcompanion, onion, HINT_MESSAGE_KEY);
    private Rule repetition = new RepetitionRule(2, carrot, RuleType.speciesrepetition, 4, HINT_MESSAGE_KEY);

    @BeforeMethod
    public void setUp() throws Exception {
        carrot.addRule(twoBrother);
        carrot.addRule(repetition);
    }

    @Test
    public void companionRuleShouldReturnHint() throws Exception {
        Garden g = new Garden();
        g.addOrGetSquare(2000, 0, 0).addSpecies(carrot);
        g.addOrGetSquare(2000, 0, 0).addSpecies(onion);
        List<Hint> hints = g.getHints();
        Assert.assertEquals(hints.size(), 1);
        Assert.assertEquals(hints.iterator().next().getMessageKey(), HINT_MESSAGE_KEY);
    }

    @Test
    public void companionRuleShouldNotReturnHint() throws Exception {
        Garden g = new Garden();
        g.addOrGetSquare(2000, 0, 0).addSpecies(carrot);
        g.addOrGetSquare(2000, 0, 2).addSpecies(onion);
        List<Hint> hints = g.getHints();
        Assert.assertEquals(hints.size(), 0);
    }

    @Test
    public void repetitionRuleShouldReturnHint() throws Exception {
        Garden g = new Garden();
        g.addOrGetSquare(2000, 0, 0).addSpecies(carrot);
        g.addOrGetSquare(2001, 0, 0).addSpecies(carrot);
        List<Hint> hints = g.getHints();
        Assert.assertEquals(hints.size(), 1);
        Assert.assertEquals(hints.iterator().next().getMessageKey(), HINT_MESSAGE_KEY);
    }

    @Test
    public void repetitionRuleShouldNotReturnHint() throws Exception {
        Garden g = new Garden();
        g.addOrGetSquare(2000, 0, 0).addSpecies(carrot);
        g.addOrGetSquare(2007, 0, 0).addSpecies(carrot);
        List<Hint> hints = g.getHints();
        Assert.assertEquals(hints.size(), 0);
    }
}