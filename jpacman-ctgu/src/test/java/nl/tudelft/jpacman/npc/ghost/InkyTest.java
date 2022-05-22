package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.npc.ghost.*;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Inky Tester.
 *
 * @author <zyj>
 * @since <pre>5 3, 2021</pre>
 * @version 1.0
 */
public class InkyTest {

    private GhostMapParser parser;
    private Player pacman;

    @Before
    public void setUp() {
        PacManSprites sprites = new PacManSprites();
        PlayerFactory playerFactory = new PlayerFactory(sprites);
        PointCalculator pointCalculator = new DefaultPointCalculator();

        BoardFactory boardFactory = new BoardFactory(sprites);
        GhostFactory ghostFactory = new GhostFactory(sprites);
        LevelFactory levelFactory = new LevelFactory(sprites, ghostFactory, pointCalculator);

        pacman = playerFactory.createPacMan();
        parser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);
    }

    /*
     * Situation_1: Inky is placed alongside Blinky, Blinky is placed behind Pac-Man. Both ghosts are behind Pac-Man
     * Tests if Inky will follow Blinky in a basic map.
     */
    @Test
    public void testInkyFollowBlinkySameRow() {
        ArrayList<String> map = Lists.newArrayList(
            "############",
            "#  IB    P #",
            "############");

        Level level = parser.parseMap(map);
        level.registerPlayer(pacman);
        pacman.setDirection(Direction.WEST);
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assert inky != null;
        assert blinky != null;
        assertThat(inky.nextAiMove()).isEqualTo(blinky.nextAiMove());
    }


    /*
     * Situation_2: Inky is placed alongside Blinky, Blinky is placed behind Pac-Man. Both ghosts are behind Pac-Man
     * Tests if Inky will follow Blinky in a more complex map.
     */
    @Test
    public void testInkyFollowBlinkyDiffRowCol() {
        List<String> map = Lists.newArrayList(
            "############",
            "##P    #####",
            "##        ##",
            "##        ##",
            "##        ##",
            "##   B     #",
            "##   I     #",
            "############");
        Level level = parser.parseMap(map);
        level.registerPlayer(pacman);
        pacman.setDirection(Direction.WEST);
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assert inky != null;
        assert blinky != null;
        assertThat(inky.nextAiMove()).isEqualTo(blinky.nextAiMove());
    }

    /*
     * Situation_3: Inky is placed in front of Pac-Man while Blinky is placed far behind Pac-Man
     * Tests if Inky will move very far ahead of Pac-Man
     * i.e South since Pac-Man is moving South.
     */
    @Test
    public void testInkyFrontBlinkyBehindPacMan() {
        List<String> map = Lists.newArrayList(
            "############",
            "##I    #####",
            "##        ##",
            "##   P    ##",
            "##        ##",
            "##         #",
            "##   B     #",
            "############");

        Level level = parser.parseMap(map);
        level.registerPlayer(pacman);
        pacman.setDirection(Direction.SOUTH);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assert inky != null;
        assertThat(inky.nextAiMove()).isEqualTo(Optional.of(Direction.SOUTH));
    }

    /*
     * Situation_4: Inky is placed in front of Pac-Man while Blinky is placed far behind Pac-Man; however, Inky is blocked
     * Tests if Inky will return empty direction.
     */
    @Test
    public void testInkyBlockedBlinkyBehindPacMan() {
        List<String> map = Lists.newArrayList(
            "##I    #####",
            "############",
            "##   P    ##",
            "##        ##",
            "##         #",
            "##   B     #",
            "############");
        Level level = parser.parseMap(map);
        level.registerPlayer(pacman);
        pacman.setDirection(Direction.SOUTH);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assert inky != null;
        assertThat(inky.nextAiMove()).isEqualTo(Optional.empty());
    }

    /*
     * Situation_5: Inky is placed in front of Blinky while Pac-Man is placed far behind Blinky;
     * Tests if Inky will move very far ahead of Pac-Man
     * i.e West since Pac-Man is moving West.
     */
    @Test
    public void testInkyBehindBlinkyFrontPacMan() {
        List<String> map = Lists.newArrayList(
            "##I    #####",
            "############",
            "##   B    ##",
            "##        ##",
            "##         #",
            "##        P#",
            "############");
        Level level = parser.parseMap(map);
        level.registerPlayer(pacman);
        pacman.setDirection(Direction.WEST);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assert inky != null;
        assertThat(inky.nextAiMove()).isEqualTo(Optional.empty());
    }
}

