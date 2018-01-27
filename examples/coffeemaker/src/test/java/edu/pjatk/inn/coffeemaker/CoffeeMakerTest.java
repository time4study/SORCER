package edu.pjatk.inn.coffeemaker;

import edu.pjatk.inn.coffeemaker.impl.CoffeeMaker;
import edu.pjatk.inn.coffeemaker.impl.Inventory;
import edu.pjatk.inn.coffeemaker.impl.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorcer.test.ProjectContext;
import org.sorcer.test.SorcerTestRunner;
import sorcer.service.ContextException;
import sorcer.service.Exertion;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.*;
import static sorcer.eo.operator.*;
import static sorcer.so.operator.eval;

/**
 * @author Mike Sobolewski
 */
@RunWith(SorcerTestRunner.class)
@ProjectContext("examples/coffeemaker")
public class CoffeeMakerTest {
	private final static Logger logger = LoggerFactory.getLogger(CoffeeMakerTest.class);

	private CoffeeMaker coffeeMaker;
	private Inventory inventory;
	private Recipe espresso, mocha, macchiato, americano;

	@Before
	public void setUp() throws ContextException {
		coffeeMaker = new CoffeeMaker();
		inventory = coffeeMaker.checkInventory();

		espresso = new Recipe();
		espresso.setName("espresso");
		espresso.setPrice(50);
		espresso.setAmtCoffee(6);
		espresso.setAmtMilk(1);
		espresso.setAmtSugar(1);
		espresso.setAmtChocolate(0);

		mocha = new Recipe();
		mocha.setName("mocha");
		mocha.setPrice(100);
		mocha.setAmtCoffee(8);
		mocha.setAmtMilk(1);
		mocha.setAmtSugar(1);
		mocha.setAmtChocolate(2);

		macchiato = new Recipe();
		macchiato.setName("macchiato");
		macchiato.setPrice(40);
		macchiato.setAmtCoffee(7);
		macchiato.setAmtMilk(1);
		macchiato.setAmtSugar(2);
		macchiato.setAmtChocolate(0);

		americano = new Recipe();
		americano.setName("americano");
		americano.setPrice(40);
		americano.setAmtCoffee(7);
		americano.setAmtMilk(1);
		americano.setAmtSugar(2);
		americano.setAmtChocolate(0);
	}

	@Test
	public void testAddRecipe() {
		assertTrue(coffeeMaker.addRecipe(espresso));
	}

	@Test
	public void testContextCofee() throws ContextException {
		assertTrue(espresso.getAmtCoffee() == 6);
	}

	@Test
	public void testContextMilk() throws ContextException {
		assertTrue(espresso.getAmtMilk() == 1);
	}

	@Test
	public void addRecepie() throws Exception {
		coffeeMaker.addRecipe(mocha);
		assertEquals(coffeeMaker.getRecipeForName("mocha").getName(), "mocha");
		assertTrue(coffeeMaker.getRecipeFull()[0]);
		assertFalse(coffeeMaker.getRecipeFull()[1]);
		assertFalse(coffeeMaker.getRecipeFull()[2]);
		assertFalse(coffeeMaker.getRecipeFull()[3]);
	}

	@Test
	public void addContextRecepie() throws Exception {
		coffeeMaker.addRecipe(Recipe.getContext(mocha));
		assertEquals(coffeeMaker.getRecipeForName("mocha").getName(), "mocha");
	}

	@Test
	public void addServiceRecepie() throws Exception {
		Exertion cmt = task(sig("addRecipe", coffeeMaker),
						context(types(Recipe.class), args(espresso),
							result("recipe/added")));

		logger.info("isAdded: " + eval(cmt));
		assertEquals(coffeeMaker.getRecipeForName("espresso").getName(), "espresso");
	}

	@Test
	public void addRecipes() throws Exception {
		coffeeMaker.addRecipe(mocha);
		coffeeMaker.addRecipe(macchiato);
		coffeeMaker.addRecipe(americano);

		assertEquals(coffeeMaker.getRecipeForName("mocha").getName(), "mocha");
		assertEquals(coffeeMaker.getRecipeForName("macchiato").getName(), "macchiato");
		assertEquals(coffeeMaker.getRecipeForName("americano").getName(), "americano");
	}

	@Test
	public void makeCoffee() throws Exception {
		int coffee = inventory.getCoffee();
		int milk = inventory.getMilk();
		int sugar = inventory.getSugar();
		int chocolate = inventory.getChocolate();
		coffeeMaker.addRecipe(espresso);
		assertEquals(coffeeMaker.makeCoffee(espresso, 200), 150);
		assertThat(inventory.getChocolate(), lessThanOrEqualTo(chocolate));
		assertThat(inventory.getSugar(), lessThanOrEqualTo(sugar));
		assertThat(inventory.getMilk(), lessThanOrEqualTo(milk));
		assertThat(inventory.getCoffee(), lessThanOrEqualTo(coffee));
	}

	@Test
	public void editRecipe() {
		Recipe newRecipe = createDefaultRecipe();
		coffeeMaker.addRecipe(espresso);
		coffeeMaker.editRecipe(espresso, newRecipe);
		assertEquals(coffeeMaker.getRecipeForName(newRecipe.getName()).getName(), newRecipe.getName());
	}

	@Test
	public void deleteRecipe() throws Exception {
		coffeeMaker.addRecipe(espresso);
		coffeeMaker.deleteRecipe(espresso);
		assertEquals(coffeeMaker.getRecipeForName("espresso"), null);
	}

	@Test
	public void addInventory() throws Exception {
		assertTrue(coffeeMaker.addInventory(10,20,5,0));
	}

	@Test
	public void checkInventory() {
		assertNotNull(coffeeMaker.checkInventory());
	}

	private Recipe createDefaultRecipe(){
		Recipe recipe = new Recipe();
		recipe.setName("COOL RECIPE");
		recipe.setPrice(1000000);
		recipe.setAmtSugar(100);
		recipe.setAmtMilk(100);
		recipe.setAmtChocolate(100);
		recipe.setAmtCoffee(100);
		return recipe;
	}
}

