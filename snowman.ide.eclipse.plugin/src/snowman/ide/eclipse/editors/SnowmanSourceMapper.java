/*
 * Based off of source code by Emmanuel Dupuy
 * This program is made available under the terms of the GPLv3 License.
 */

package snowman.ide.eclipse.editors;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import jd.commonide.IdeDecompiler;
import jd.commonide.preferences.IdePreferences;
import snowman.ide.eclipse.SnowmanDecompilerPlugin;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.internal.core.SourceMapper;
import org.eclipse.jface.preference.IPreferenceStore;


/**
 * JDSourceMapper
 * 
 * @project Snowman Decompiler Plug-In
 * @version 0.0.1
 * @see     org.eclipse.jdt.internal.core.SourceMapper
 */
@SuppressWarnings("restriction")
public class SnowmanSourceMapper extends SourceMapper
{
	private final static String JAVA_CLASS_SUFFIX         = ".class";
	private final static String JAVA_SOURCE_SUFFIX        = ".java";
	private final static int    JAVA_SOURCE_SUFFIX_LENGTH = 5;

	private File basePath;
	
	@SuppressWarnings("rawtypes")
	public SnowmanSourceMapper(
		File basePath, IPath sourcePath, String sourceRootPath, Map options) 
	{	
		super(sourcePath, sourceRootPath, options);
		this.basePath = basePath;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public char[] findSource(String javaSourcePath) 
	{
		char[] source = null;
		
		// Search source file
		if (this.rootPaths == null)
		{
			source = super.findSource(javaSourcePath);
		}
		else
		{
			Iterator iterator = this.rootPaths.iterator();
			while (iterator.hasNext() && (source == null))
			{
				String sourcesRootPath = (String)iterator.next();				
				source = super.findSource(
					sourcesRootPath + IPath.SEPARATOR + javaSourcePath);
			}
		}
		
		if ((source == null) && javaSourcePath.endsWith(JAVA_SOURCE_SUFFIX))
		{	
			String classPath = javaSourcePath.substring(
				0, javaSourcePath.length()-JAVA_SOURCE_SUFFIX_LENGTH) + JAVA_CLASS_SUFFIX;
			
			// Decompile class file
			try
			{
				String result = decompile(this.basePath.getAbsolutePath(), classPath);
				if (result != null)
					source = result.toCharArray();
			}
			catch (Exception e)
			{
				SnowmanDecompilerPlugin.getDefault().getLog().log(new Status(
					IStatus.ERROR, SnowmanDecompilerPlugin.PLUGIN_ID, 
					0, e.getMessage(), e));
			}
		}

		return source;
	}
		
    /**
     * @param basePath          Path to the root of the classpath, either a 
     *                          path to a directory or a path to a jar file.
     * @param internalClassName internal name of the class.
     * @return Decompiled class text.
     */
	protected String decompile(String basePath, String classPath) {
		// Load preferences
		IPreferenceStore store = SnowmanDecompilerPlugin.getDefault().getPreferenceStore();
		
		boolean showDefaultConstructor = store.getBoolean(SnowmanDecompilerPlugin.PREF_SHOW_DEFAULT_CONSTRUCTOR);
		boolean realignmentLineNumber = store.getBoolean(SnowmanDecompilerPlugin.PREF_REALIGN_LINE_NUMBERS);
		boolean showPrefixThis = store.getBoolean(SnowmanDecompilerPlugin.PREF_OMIT_PREFIX_THIS);
		boolean mergeEmptyLines = false;
		boolean unicodeEscape = store.getBoolean(SnowmanDecompilerPlugin.PREF_ESCAPE_UNICODE_CHARACTERS);
		boolean showLineNumbers = store.getBoolean(SnowmanDecompilerPlugin.PREF_SHOW_LINE_NUMBERS);
		boolean showMetadata = store.getBoolean(SnowmanDecompilerPlugin.PREF_SHOW_METADATA);
		
		// Create preferences
		IdePreferences preferences = new IdePreferences(
			showDefaultConstructor, realignmentLineNumber, showPrefixThis, 
			mergeEmptyLines, unicodeEscape, showLineNumbers, showMetadata);
		
		// Decompile
		return IdeDecompiler.decompile(preferences, basePath, classPath);
	}
	
    /**
     * @return version of JD-Core
     * @since JD-Core 0.7.0
     */
    public static String getVersion() { return "0.7.1"; }
}
