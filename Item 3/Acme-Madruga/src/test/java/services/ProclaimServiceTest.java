
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Proclaim;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ProclaimServiceTest extends AbstractTest {

	// Services
	@Autowired
	private ProclaimService	proclaimService;


	@Test
	public void driver1() {
		final Object testingData[][] = {
			//En la cobertura de datos no se contempla moment ya que se setea automaticamente en el sistema.
			{
				//				A: Acme Madruga Req. 17 -> Chapters can publish proclaims
				//				B: Test Negativo: --
				//				C: 100% Recorre 24 de las 24 lineas posibles
				//				D: cobertura de datos=15/15
				"chapter1", "descriptionTest", null
			}, {
				//				A: Acme Madruga Req. 17 -> Chapters can publish proclaims
				//				B: Test Negativo: Usuario null
				//				C: 10% Recorre 2 de la 24 lineas posibles
				//				D: cobertura de datos=15/15
				null, "descriptionTest", IllegalArgumentException.class
			}, {
				//				A: Acme Madruga Req. 17 -> Chapters can publish proclaims
				//				B: Test Negativo: Registro con un member
				//				C: 10% Recorre 2 de la 24 lineas posibles
				//				D: cobertura de datos=15/15
				"member1", "descriptionTest", IllegalArgumentException.class
			}, {
				//				A: Acme Madruga Req. 12 -> the system must store the moment when it's published and a piece of text that can't be longer than 250 characters
				//				B: Test Negativo: Text cadena vacia
				//				C: 100% Recorre 24 de la 24 lineas posibles(Al saltar con flush se ejecuta el 100%)
				//				D: cobertura de datos=15/15
				"chapter1", "", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	@Test
	public void driver2() {
		final Object testingData[][] = {

			{
				//				A: Acme Madruga Req. 12 -> the system must store the moment when it's published and a piece of text that can't be longer than 250 characters
				//				B: Test Positivo: text = 250
				//				C: 100% Recorre 24 de la 24 lineas posibles(Al saltar con flush se ejecuta el 100%)
				//				D: cobertura de datos=15/15
				"chapter1",
				"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
					+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", null
			},
			{
				//				A: Acme Madruga Req. 12 -> the system must store the moment when it's published and a piece of text that can't be longer than 250 characters
				//				B: Test Negativo: text = 251
				//				C: 100% Recorre 24 de la 24 lineas posibles(Al saltar con flush se ejecuta el 100%)
				//				D: cobertura de datos=15/15
				"chapter1",
				"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
					+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", ConstraintViolationException.class
			}, {
				//				A: Acme Madruga Req. 12 -> the system must store the moment when it's published and a piece of text that can't be longer than 250 characters
				//				B: Test Negativo: text null
				//				C: 100% Recorre 24 de la 24 lineas posibles(Al saltar con flush se ejecuta el 100%)
				//				D: cobertura de datos=15/15
				"chapter1", null, NullPointerException.class
			}, {
				//				A: Acme Madruga Req. 17.1 ->  once a proclaim is published, there's no way to update or delete it
				//				B: Test Negativo: Modificar proclaim ya guardada
				//				C: 66,67% Recorre 16 de la 24 lineas posibles
				//				D: cobertura de datos=15/15
				"chapter1", "edit", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void templateCreateSave(final String user, final String text, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			final Proclaim proc = this.proclaimService.create();
			proc.setText(text);
			final Proclaim procSaved = this.proclaimService.save(proc);
			Assert.isTrue(procSaved.getId() != 0);
			if (text.equals("edit"))
				this.proclaimService.save(procSaved);
			this.proclaimService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
