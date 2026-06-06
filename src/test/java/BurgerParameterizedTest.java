import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import praktikum.Bun;
import praktikum.Burger;
import praktikum.Ingredient;
import praktikum.IngredientType;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class BurgerParameterizedTest {

    private Burger burger;
    @Mock
    private Bun bunMock;

    private String testName;
    private String bunName;        // Имя булочки из Database
    private float bunPrice;       // Цена булочки из Database
    private Object[][] ingredientData;
    private float expectedPrice;
    private String[] expectedLines;

    public BurgerParameterizedTest(String testName, String bunName, float bunPrice,
                                   Object[][] ingredientData, float expectedPrice, String[] expectedLines) {
        this.testName = testName;
        this.bunName = bunName;
        this.bunPrice = bunPrice;
        this.ingredientData = ingredientData;
        this.expectedPrice = expectedPrice;
        this.expectedLines = expectedLines;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                // Только булочка (чёрная)
                {"Only black bun", "black bun", 100f, new Object[][]{}, 200f,
                        new String[]{"(==== black bun ====)", "Price: 200"}},

                // Чёрная булочка + котлета
                {"Black bun + cutlet", "black bun", 100f,
                        new Object[][]{{"cutlet", IngredientType.FILLING, 100f}},
                        300f,  // 100×2 + 100
                        new String[]{
                                "(==== black bun ====)",
                                "= filling cutlet =",
                                "Price: 300"
                        }},

                // Белая булочка + острый соус
                {"White bun + hot sauce", "white bun", 200f,
                        new Object[][]{{"hot sauce", IngredientType.SAUCE, 100f}},
                        500f,  // 200×2 + 100
                        new String[]{
                                "(==== white bun ====)",
                                "= sauce hot sauce =",
                                "Price: 500"
                        }},

                // Красная булочка + динозавр + сметана
                {"Red bun + dinosaur + sour cream", "red bun", 300f,
                        new Object[][]{
                                {"dinosaur", IngredientType.FILLING, 200f},
                                {"sour cream", IngredientType.SAUCE, 200f}
                        },
                        1000f,  // 300×2 + 200 + 200
                        new String[]{
                                "(==== red bun ====)",
                                "= filling dinosaur =",
                                "= sauce sour cream =",
                                "Price: 1000"
                        }}
        });
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        burger = new Burger();

        // мок булочки
        Mockito.when(bunMock.getPrice()).thenReturn(bunPrice);
        Mockito.when(bunMock.getName()).thenReturn(bunName);
        burger.setBuns(bunMock);

        // ингредиенты для теста
        for (Object[] ingredientInfo : ingredientData) {
            String name = (String) ingredientInfo[0];
            IngredientType type = (IngredientType) ingredientInfo[1];
            float price = (float) ingredientInfo[2];

            Ingredient ingredientMock = Mockito.mock(Ingredient.class);
            Mockito.when(ingredientMock.getName()).thenReturn(name);
            Mockito.when(ingredientMock.getType()).thenReturn(type);
            Mockito.when(ingredientMock.getPrice()).thenReturn(price);

            burger.addIngredient(ingredientMock);
        }
    }

    @Test
    public void burgerPriceTest() {
        //цена
        float actualPrice = burger.getPrice();
        assertEquals(expectedPrice, actualPrice, 0.001f);}


    @Test
    public void burgerReceiptTest() {
    //чек
        String receipt = burger.getReceipt();

        for (String expectedLine : expectedLines) {
            assertTrue(receipt.contains(expectedLine));
        }
    }
}
