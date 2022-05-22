package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * MapParser Tester.
 *
 * @author <zyj>
 * @since <pre>5月 4, 2021</pre>
 * @version 1.0
 */
public class MapParserTest {
    @Mock
    private BoardFactory boardmock = mock(BoardFactory.class);
    @Mock
    private  LevelFactory levelmock = mock(LevelFactory.class);
    @Mock
    private GhostFactory ghostmock = mock(GhostFactory.class);

    MapParser mapParser = new MapParser(levelmock,boardmock);

    @Before
    public  void before(){
        //模拟出棋盘中的成分：墙和地面
        when(boardmock.createGround()).thenReturn(mock(Square.class));
        when(boardmock.createWall()).thenReturn(mock(Square.class));
        //模拟创造出魔鬼和小豆人
        when(levelmock.createGhost()).thenReturn(mock(Ghost.class));
        when(levelmock.createPellet()).thenReturn(mock(Pellet.class));

    }

    /**
     *
     * Method: parseMap(char[][] map)
     * Description:第二关：输入为{"##", " "}时，createWall应该被调用两次，
     * createGround应该被调用两次。请用Mock来验证这个过程。
     * 除此之外，构建另外两个正常的情况，用Mock来验证这个过程。
     * */
    @Test
    public void testParseMapMap(){
        char[][] map = new char[][]{{'#','#'}, {' ',' '}};
        mapParser.parseMap(map);
        //验证createWall应该被调用两次
        verify(boardmock,times(2)).createWall();
        //验证createGround应该被调用两次
        verify(boardmock,times(2)).createGround();
        //验证至少调用createwall一次的情况（atLeastOnce：至少一次 atMostOnce：最多一次 atLeast(int)：至少X次  atMost(int)：最多X次）
        verify(boardmock,atLeastOnce()).createWall();
        //验证类没有被调用过
//    verifyNoMoreInteractions(levelmock);//验证成功，已调用.
        verifyNoMoreInteractions(ghostmock);
    }
    /**
     * Method: parseMap2(List<String> text)
     * Description:第3关：将上面的Mock测试，推广到输入错误的情况。比如，输入为{"##", " "}时，
     * 由于第一行有两个Square，第二行有三个Square。这种情况下，程序应该能够抛出异常。mock这一过程，
     * 采用断言判断你的测试程序能否捕获这样的异常。除此之外，构建另外两个异常的情况，
     * 用mock程序来进行断言判断程序是否正确处理这种异常。
     * */
    @Test
    public void test2ParseMapMap(){
        List<String> text=new ArrayList(Arrays.asList(new String[]{"##", "   "}));
        try {
            mapParser.parseMap(text);
        }catch (Exception e){
            for (int i = 0;i < text.get(0).length()-1;i++){
                assertThat(text.get(i).length()).isEqualTo(text.get(i+1).length());
            }
        }
    }
    /**
     * Method: test2_1ParseMapMap
     * Description:第3关异常：Input text cannot be null.。
     * */
    @Test
    public void test2_1ParseMapMap(){
        List<String> text=new ArrayList(Arrays.asList(new String[]{"##",null}));
        try {
            mapParser.parseMap(text);
        }catch (Exception e){
            for (String s : text) assertThat(s).isNotNull();
        }
    }

    /**
     * Method: test2_2ParseMapMap
     * Description:第3关异常：Input text must consist of at least 1 row.。
     * */
    @Test
    public void test2_2ParseMapMap(){
        List<String> text=new ArrayList(Arrays.asList(new String[]{}));
        try {
            mapParser.parseMap(text);
        }catch (Exception e){
            assertThat(text.size()).isNotEqualTo(0);
        }
    }
}
