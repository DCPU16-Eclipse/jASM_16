/**
 * Copyright 2012 Tobias Gierke <tobias.gierke@code-sourcery.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.codesourcery.jasm16.compiler;

import java.util.List;
import java.util.NoSuchElementException;

import de.codesourcery.jasm16.ast.IncludeSourceFileNode;
import de.codesourcery.jasm16.compiler.io.IObjectCodeWriterFactory;
import de.codesourcery.jasm16.compiler.io.IResourceResolver;
import de.codesourcery.jasm16.exceptions.UnknownCompilationOrderException;

/**
 * Compiler (assembler).
 * 
 * <p>The compiler (assembler) is actually implemented in terms
 * of generic compilation phases that are executed one after another until
 * either compilation fails (and the phase indicates that compilation must 
 * not continue in this case) or there are no more phases to be executed.
 * </p>
 * <p>Each compiler phase needs to have a unique name (identifier) that is being
 * used to refer to the phase). 
 * </p> 
 * 
 * @author tobias.gierke@code-sourcery.de
 */
public interface ICompiler 
{
	public enum CompilerOption {
		/**
		 * Enables verbose debug output.
		 */
		DEBUG_MODE,
		/**
		 * When enabled , the parser parses mnemonics
		 * case-insensitive (default is to require upper-case, "SET a,1" is ok but "set a,1" isn't). 
		 */
		RELAXED_PARSING,
		/**
		 * Whether to actually process source includes
		 * (and thus include their AST into the AST of
		 * the current compilation unit) or just acknowledge
		 * that their is an include (by just adding a {@link IncludeSourceFileNode} without
		 * an AST as child node).
		 */
		NO_SOURCE_INCLUDE_PROCESSING; // disable .include processing
	}
	
	/**
	 * Enable/disable a compiler flag.
	 * 
	 * @param option
	 * @param onOff
	 */
	public void setCompilerOption(CompilerOption option,boolean onOff);
	
	/**
	 * Check whether a specific compiler option is enabled.
	 * 
	 * @param option
	 * @return
	 */
	public boolean hasCompilerOption(CompilerOption option);
	
	/**
	 * Compiles a set of compilation units.
	 * 
	 * <p>The compilation units will be compiled/linked in the order determined
	 * by the current {@link ICompilationOrderProvider}.</p>
	 * 
	 * @param units
	 * @return processed compilation units
	 * @see #setCompilationOrderProvider(ICompilationOrderProvider)
     * @throws UnknownCompilationOrderException if the compiler's {@link ICompilationOrderProvider} failed to determine the compilation order	 
	 */
	public void compile(List<ICompilationUnit> units) throws UnknownCompilationOrderException;	
	
	/**
	 * Compiles a set of compilation units , notifying a {@link ICompilationListener}
	 * instance while doing so.
	 * 
     * <p>The compilation units will be compiled/linked in the order determined
     * by the current {@link ICompilationOrderProvider}.</p>
     * 	 
	 * @param units
	 * @param listener
	 * @return processed compilation units
	 * @see #setCompilationOrderProvider(ICompilationOrderProvider)
	 * @throws UnknownCompilationOrderException if the compiler's {@link ICompilationOrderProvider} failed to determine the compilation order
	 */
	public void compile(List<ICompilationUnit> units,ICompilationListener listener) throws UnknownCompilationOrderException;
	
	/**
	 * Returns all compiler phases currently that are currently configured.
	 * 
	 * <p>Compilation phases will run in the same order as returned by this method.</p>
	 * 
	 * @return
	 */
	public List<ICompilerPhase> getCompilerPhases();
	
	/**
	 * Inserts a new compilation phase to be run before an already existing one.
	 * @param phase
	 * @param name
	 */
	public void insertCompilerPhaseBefore(ICompilerPhase phase,String name);
	
	/**
	 * Replaces an already configured compilation phase with another one.
	 * 
	 * @param phase
	 * @param name
	 */	
	public void replaceCompilerPhase(ICompilerPhase phase,String name);
	
	/**
	 * Inserts a new compilation phase to be run after an already configured one.
	 * 
	 * @param phase
	 * @param name
	 */
	public void insertCompilerPhaseAfter(ICompilerPhase phase,String name);
	
	/**
	 * Removes a compiler phase phase from the configuration.
	 * 
	 * @param name
	 */
	public void removeCompilerPhase(String name);
	
	/**
	 * Look up a configured compiler phase by name.
	 * 
	 * @param name
	 * @return
	 * @throws NoSuchElementException
	 */
	public ICompilerPhase getCompilerPhaseByName(String name) throws NoSuchElementException;
	
	/**
	 * Sets the factory to use when object code output writers.
	 *  
	 * @param factory
	 */
	public void setObjectCodeWriterFactory(IObjectCodeWriterFactory factory);
	
	/**
	 * Sets the resource resolver to use when resolving includes etc.
	 * @param resolver
	 */
	public void setResourceResolver(IResourceResolver resolver);
	
	/**
	 * Sets the compilation order provider responsible for determining the
	 * compilation order when more than one compilation unit is to be compiled.
	 * 
	 * <p>By default the compiler uses an implementation that will link/compile
	 * compilation units in the order they where passed to {@link #compile(List)}.</p>
	 * 
	 * @param provider
	 */
	public void setCompilationOrderProvider(ICompilationOrderProvider provider);
}