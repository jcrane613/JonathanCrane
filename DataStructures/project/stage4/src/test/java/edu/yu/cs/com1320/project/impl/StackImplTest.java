package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.stage4.impl.DocumentImpl;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class StackImplTest {

	private StackImpl<GenericCommand> stack;
	private GenericCommand cmd1;
	private GenericCommand cmd2;

	@Before
	public void initVariables() throws URISyntaxException {
		this.stack = new StackImpl<GenericCommand>();
		//uri & cmd 1
		URI uri1 = new URI("http://www.test1.net");
		this.cmd1 = new GenericCommand(uri1, target ->{
			return target.equals(uri1);
		});
		//uri & cmd 2
		URI uri2 = new URI("http://www.test2.net");
		this.cmd2 = new GenericCommand(uri2, target ->{
			return target.equals(uri2);
		});
		this.stack.push(this.cmd1);
		this.stack.push(this.cmd2);
	}

	@Test
	public void pushAndPopTest(){
		GenericCommand pcmd = stack.pop();
		assertEquals("first pop should've returned second command",this.cmd2,pcmd);
		pcmd = stack.pop();
		assertEquals("second pop should've returned first command",this.cmd1,pcmd);
	}

	@Test
	public void peekTest(){
		GenericCommand pcmd = this.stack.peek();
		assertEquals("first peek should've returned second command",this.cmd2,pcmd);
		pcmd = this.stack.pop();
		assertEquals("first pop should've returned second command",this.cmd2,pcmd);

		pcmd = this.stack.peek();
		assertEquals("second peek should've returned first command",this.cmd1,pcmd);
		pcmd = this.stack.pop();
		assertEquals("second pop should've returned first command",this.cmd1,pcmd);
	}
	@Test
	public void sizeTest(){
		assertEquals("two commands should be on the stack",2,this.stack.size());
		this.stack.peek();
		assertEquals("peek should not have affected the size of the stack",2,this.stack.size());
		this.stack.pop();
		assertEquals("one command should be on the stack after one pop",1,this.stack.size());
		this.stack.peek();
		assertEquals("peek still should not have affected the size of the stack",1,this.stack.size());
		this.stack.pop();
		assertEquals("stack should be empty after 2 pops",0,this.stack.size());
	}
	@Test
	public void interfaceCount() {//tests that the class only implements one interface and its the correct one
		@SuppressWarnings("rawtypes")
		Class[] classes = StackImpl.class.getInterfaces();
		assertTrue(classes.length == 1);
		assertTrue(classes[0].getName().equals("edu.yu.cs.com1320.project.Stack"));
	}

	@Test
	public void methodCount() {//need only test for non constructors
		Method[] methods = StackImpl.class.getDeclaredMethods();
		int publicMethodCount = 0;
		for (Method method : methods) {
			if (Modifier.isPublic(method.getModifiers())) {
				if(!method.getName().equals("equals") && !method.getName().equals("hashCode")) {
					publicMethodCount++;
				}
			}
		}
		assertTrue(publicMethodCount == 4);
	}

	@Test
	public void fieldCount() {
		Field[] fields = DocumentImpl.class.getFields();
		int publicFieldCount = 0;
		for (Field field : fields) {
			if (Modifier.isPublic(field.getModifiers())) {
				publicFieldCount++;
			}
		}
		assertTrue(publicFieldCount == 0);
	}

	@Test
	public void subClassCount() {
		@SuppressWarnings("rawtypes")
		Class[] classes = DocumentImpl.class.getClasses();
		assertTrue(classes.length == 0);
	}

	@Test
	public void noArgsConstructorExists(){
		try {
			new StackImpl();
		} catch (RuntimeException e) {}
	}
}