
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
    private Ingredient ingredientMock1;
    @Mock
    private Ingredient ingredientMock2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        burger = new Burger();
    }

    // Для булочек
    @Test
    public void testSetBunsShouldSetBun() {
        Mockito.when(bunMock.getPrice()).thenReturn(100.0f);
        Mockito.when(bunMock.getName()).thenReturn("test bun");

        burger.setBuns(bunMock);

        assertSame(bunMock, burger.bun);
    }

    // Добавленин ингредиентов
    @Test
    public void testAddIngredientShouldAddIngredientToList() {
        Mockito.when(ingredientMock1.getPrice()).thenReturn(50.0f);

        burger.addIngredient(ingredientMock1);

        assertEquals(1, burger.ingredients.size());
        assertSame(ingredientMock1, burger.ingredients.get(0));
    }

    // Удаление ингредиента по индексу
    @Test
    public void testRemoveIngredientShouldRemoveIngredientByIndex() {
        Mockito.when(ingredientMock1.getPrice()).thenReturn(50.0f);
        Mockito.when(ingredientMock2.getPrice()).thenReturn(75.0f);

        burger.addIngredient(ingredientMock1);
        burger.addIngredient(ingredientMock2);

        burger.removeIngredient(0);

        assertEquals(1, burger.ingredients.size());
        assertSame(ingredientMock2, burger.ingredients.get(0));
    }

    // Перемещение ингредиента
    @Test
    public void testMoveIngredientShouldMoveIngredientToNewIndex() {
        Mockito.when(ingredientMock1.getPrice()).thenReturn(50.0f);
        Mockito.when(ingredientMock2.getPrice()).thenReturn(75.0f);

        burger.addIngredient(ingredientMock1);
        burger.addIngredient(ingredientMock2);

        burger.moveIngredient(0, 1);

        assertEquals(2, burger.ingredients.size());
        assertSame(ingredientMock2, burger.ingredients.get(0));
        assertSame(ingredientMock1, burger.ingredients.get(1));
    }

    // Тест расчёта цены — несколько сценариев в одном тесте
    @Test
    public void testGetPrice_ShouldCalculateCorrectPrice() {
        // Сценарий 1: только булочка
        Mockito.when(bunMock.getPrice()).thenReturn(100.0f);
        burger.setBuns(bunMock);
        assertEquals(200.0f, burger.getPrice(), 0.001f);

        // Сценарий 2: булочка + один ингредиент
        Ingredient ingredient1 = Mockito.mock(Ingredient.class);
        Mockito.when(ingredient1.getPrice()).thenReturn(50.0f);
        burger.addIngredient(ingredient1);
        assertEquals(250.0f, burger.getPrice(), 0.001f);

        // Сценарий 3: булочка + два ингредиента
        Ingredient ingredient2 = Mockito.mock(Ingredient.class);
        Mockito.when(ingredient2.getPrice()).thenReturn(75.0f);
        burger.addIngredient(ingredient2);
        assertEquals(325.0f, burger.getPrice(), 0.001f);
    }

    // Тест формирования чека
    @Test
    public void testGetReceipt_ShouldGenerateCorrectReceipt() {
        // Настраиваем мок булочки
        Mockito.when(bunMock.getPrice()).thenReturn(100.0f);
        Mockito.when(bunMock.getName()).thenReturn("black bun");

        // Создаём моки ингредиентов с чёткими значениями
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

        // Проверяем наличие булочек в начале и конце
        assertTrue(receipt.contains("(==== black bun ====)"));

        // Проверяем ингредиенты с учётом возможного форматирования
        assertTrue(receipt.contains("= filling cutlet =") || receipt.contains("=filling cutlet="));
        assertTrue(receipt.contains("= sauce hot sauce =") || receipt.contains("=sauce hot sauce="));

        // Проверяем цену с допуском на точность float
        assertTrue(receipt.contains("Price: 550") ||
                receipt.contains("Price: 550.0") ||
                receipt.contains("Price: 550.00"));
    }

}
