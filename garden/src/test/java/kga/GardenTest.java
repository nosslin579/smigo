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
    private Family mockFamily = new Family(1, "Mock Family");
    private Species carrot = new Species(1, new HashSet<Rule>(), mockFamily, true, false);
    private Species onion = new Species(2, new HashSet<Rule>(), mockFamily, true, false);
    private Species item = new Species(3, new HashSet<Rule>(), mockFamily, true, true);
    private Rule twoBrother = new CompanionRule(1, carrot, RuleType.goodcompanion, onion, HINT_MESSAGE_KEY);
    private Rule repetition = new RepetitionRule(2, carrot, RuleType.speciesrepetition, 4, HINT_MESSAGE_KEY);
    private Garden g;

    @BeforeMethod
    public void setUp() throws Exception {
        g = new Garden();
        g.addOrGetSquare(2000, 0, 0).addSpecies(carrot);
        g.addOrGetSquare(2000, 1, 0).addSpecies(item);
        carrot.addRule(twoBrother);
        carrot.addRule(repetition);
    }

    @Test
    public void companionRuleShouldReturnHint() throws Exception {
        g.addOrGetSquare(2000, 0, 0).addSpecies(onion);
        List<Hint> hints = g.getHints();
        Assert.assertEquals(hints.size(), 1);
        Assert.assertEquals(hints.iterator().next().getMessageKey(), HINT_MESSAGE_KEY);
    }

    @Test
    public void companionRuleShouldNotReturnHint() throws Exception {
        g.addOrGetSquare(2000, 0, 2).addSpecies(onion);
        List<Hint> hints = g.getHints();
        Assert.assertEquals(hints.size(), 0);
    }

    @Test
    public void repetitionRuleShouldReturnHint() throws Exception {
        g.addOrGetSquare(2001, 0, 0).addSpecies(carrot);
        List<Hint> hints = g.getHints();
        Assert.assertEquals(hints.size(), 1);
        Assert.assertEquals(hints.iterator().next().getMessageKey(), HINT_MESSAGE_KEY);
    }

    @Test
    public void repetitionRuleShouldNotReturnHint() throws Exception {
        g.addOrGetSquare(2007, 0, 0).addSpecies(carrot);
        List<Hint> hints = g.getHints();
        Assert.assertEquals(hints.size(), 0);
    }

    @Test
    public void addYear() throws Exception {
        g.addYear(2001);
        List<Square> squaresFor = g.getSquaresFor(2001);
        Assert.assertEquals(squaresFor.size(), 1);
        Assert.assertEquals(squaresFor.iterator().next().getYearXY(), new YearXY(2001, 1, 0));
    }
}