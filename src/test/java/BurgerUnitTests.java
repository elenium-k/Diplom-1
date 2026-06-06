
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import praktikum.Bun;
import praktikum.Burger;
import praktikum.Ingredient;
import praktikum.IngredientType;

import static org.junit.Assert.*;

public class BurgerUnitTests {

    private Burger burger;
    @Mock
    private Bun bunMock;
    @Mock
    private Ingredient ingredientFillingMock;
    @Mock
    private Ingredient ingredientSauceMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        burger = new Burger();
    }

    // Для булочек
    @Test
    public void setBunTest() {
        Mockito.when(bunMock.getPrice()).thenReturn(100.0f);
        Mockito.when(bunMock.getName()).thenReturn("test bun");

        burger.setBuns(bunMock);

        assertSame(bunMock, burger.bun);
    }

    // Добавленин ингредиентов
    @Test
    public void addIngredientToListTest() {
        Mockito.when(ingredientFillingMock.getPrice()).thenReturn(50.0f);

        burger.addIngredient(ingredientFillingMock);

        assertEquals(1, burger.ingredients.size());
        assertSame(ingredientFillingMock, burger.ingredients.get(0));
    }

    // Удаление ингредиента по индексу
    @Test
    public void removeIngredientByIndexTest() {
        Mockito.when(ingredientFillingMock.getPrice()).thenReturn(50.0f);
        Mockito.when(ingredientSauceMock.getPrice()).thenReturn(75.0f);

        burger.addIngredient(ingredientFillingMock);
        burger.addIngredient(ingredientSauceMock);

        burger.removeIngredient(0);

        assertEquals(1, burger.ingredients.size());
        assertSame(ingredientSauceMock, burger.ingredients.get(0));
    }

    // Перемещение ингредиента
    @Test
    public void moveIngredientToNewIndexTest() {
        Mockito.when(ingredientFillingMock.getPrice()).thenReturn(50.0f);
        Mockito.when(ingredientSauceMock.getPrice()).thenReturn(75.0f);

        burger.addIngredient(ingredientFillingMock);
        burger.addIngredient(ingredientSauceMock);

        burger.moveIngredient(0, 1);

        assertEquals(2, burger.ingredients.size());
        assertSame(ingredientSauceMock, burger.ingredients.get(0));
        assertSame(ingredientFillingMock, burger.ingredients.get(1));
    }

    // Тест расчёта цены
    @Test
    public void getPriceTest() {
        // Сценарий 1: только булочка
        Mockito.when(bunMock.getPrice()).thenReturn(100.0f);
        burger.setBuns(bunMock);
        assertEquals(200.0f, burger.getPrice(), 0.001f);
    }

    // Тест формирования чека
    @Test
    public void getReceiptTest() {

        Mockito.when(bunMock.getPrice()).thenReturn(100.0f);
        Mockito.when(bunMock.getName()).thenReturn("black bun");

        Ingredient fillingMock = Mockito.mock(Ingredient.class);
        Ingredient sauceMock = Mockito.mock(Ingredient.class);

        Mockito.when(fillingMock.getPrice()).thenReturn(150.0f);
        Mockito.when(fillingMock.getName()).thenReturn("cutlet");
        Mockito.when(fillingMock.getType()).thenReturn(IngredientType.FILLING);

        Mockito.when(sauceMock.getPrice()).thenReturn(200.0f);
        Mockito.when(sauceMock.getName()).thenReturn("hot sauce");
        Mockito.when(sauceMock.getType()).thenReturn(IngredientType.SAUCE);

        // Собираем бургер
        burger.setBuns(bunMock);
        burger.addIngredient(fillingMock);
        burger.addIngredient(sauceMock);

        String receipt = burger.getReceipt();

        // Проверяем цену с допуском на точность float
        assertTrue(receipt.contains("Price: 550") ||
                receipt.contains("Price: 550.0") ||
                receipt.contains("Price: 550.00"));
    }

}