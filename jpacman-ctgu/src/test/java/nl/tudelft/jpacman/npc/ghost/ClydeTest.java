package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Clyde Tester.
 *
 * @author <zyj>
 * @since <pre>5 3, 2021</pre>
 * @version 1.0
 */
public class ClydeTest {
    private GhostMapParser parser;
    private Player pacman;

    @Before
    public void setUp(){
        PacManSprites sprites = new PacManSprites();
        PlayerFactory playerFactory = new PlayerFactory(sprites);
        PointCalculator pointCalculator = new DefaultPointCalculator();

        BoardFactory boardFactory = new BoardFactory(sprites);
        GhostFactory ghostFactory = new GhostFactory(sprites);
        LevelFactory levelFactory = new LevelFactory(sprites,ghostFactory,pointCalculator);

        pacman = playerFactory.createPacMan();
        parser = new GhostMapParser(levelFactory,boardFactory,ghostFactory);

    }

    /*
     * Clyde has two AIs:
     *       1. "Clyde is Shy." Clyde will run TOWARDS Pac-Man
     *          if the number of spaces between Clyde and Pac-Man >= 8
     *       2. "Clyde is not Shy." Clyde will run AWAY from Pac-Man
     *          if the number of spaces between Clyde and Pac-Man < 8
     */

    /*
     * Clyde and Pac-Man are on the same row, 6 spaces apart.
     * Tests if Clyde can find the path towards Pac-Man and run away from it.
     */
    @Test
    public void testClydeShySameRow(){
        ArrayList<String> map = Lists.newArrayList(
            "############",
            "#P      C###",
            "############");

        Level level = parser.parseMap(map);
        level.registerPlayer(pacman);
        pacman.setDirection(Direction.EAST);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assert clyde != null;
        assertThat(clyde.nextAiMove()).isEqualTo(Optional.of(Direction.EAST));
    }

    /*
     * Clyde and Pac-Man are on different rows and columns, 10 spaces apart.
     * Tests if Clyde can find the path towards Pac-Man and run towards it.
     */
    @Test
    public void testClydeNotShyDiffRowCol(){
        ArrayList<String> map = Lists.newArrayList(
            "############",
            "#P##########",
            "# ##########",
            "#  #########",
            "## #########",
            "##      C###",
            "############");

        Level level = parser.parseMap(map);
        level.registerPlayer(pacman);
        pacman.setDirection(Direction.SOUTH);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assert clyde != null;
        assertThat(clyde.nextAiMove()).isEqualTo(Optional.of(Direction.WEST));
    }


    /*
     * Clyde and Pac-Man are on different rows and columns, 11 spaces apart.
     * The path between Pac-Man and Clyde is blocked.
     * Tests if Clyde knows that the path is indeed blocked.
     */
    @Test
    public void testBlockedPath(){
        ArrayList<String> map = Lists.newArrayList(
            "############",
            "#P##########",
            "# ##########",
            "############",
            "############",
            "# ##########",
            "# ##########",
            "#      C####",
            "############");

        Level level = parser.parseMap(map);
        level.registerPlayer(pacman);
        pacman.setDirection(Direction.SOUTH);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assert clyde != null;
        assertThat(clyde.nextAiMove()).isEqualTo(Optional.empty());
    }

    /*
     * Pac-Man is not on the board.
     * Tests if Clyde knows that there is no path/target.
     */
    @Test
    public void testNoPacMan(){
        ArrayList<String> map = Lists.newArrayList(
            "############",
            "#      C####",
            "############");
        Level level = parser.parseMap(map);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assert clyde != null;
        assertThat(clyde.nextAiMove()).isEqualTo(Optional.empty());
    }

}

