/*
 * Based off of source code by Emmanuel Dupuy
 * This program is made available under the terms of the GPLv3 License.
 */

package snowman.ide.eclipse;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.EditorRegistry;
import org.eclipse.ui.internal.registry.FileEditorMapping;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @project Snowman Decompiler Plug-In
 * @version 0.0.1
 */
@SuppressWarnings({ "restriction", "deprecation" })
public class SnowmanDecompilerPlugin extends AbstractUIPlugin 
{
	// The plug-in IDs
	public  static final String PLUGIN_ID = "sd.ide.eclipse";
	private static final String EDITOR_ID = PLUGIN_ID + ".editors.SDClassFileEditor";	
	
	// Versions
	public static final String VERSION_SD_ECLIPSE = "0.0.1";

	// Preferences
	public static final String PREF_ESCAPE_UNICODE_CHARACTERS   = PLUGIN_ID + ".prefs.EscapeUnicodeCharacters";
	public static final String PREF_OMIT_PREFIX_THIS            = PLUGIN_ID + ".prefs.OmitPrefixThis";
	public static final String PREF_REALIGN_LINE_NUMBERS        = PLUGIN_ID + ".prefs.RealignLineNumbers";
	public static final String PREF_SHOW_LINE_NUMBERS           = PLUGIN_ID + ".prefs.ShowLineNumbers";
	public static final String PREF_SHOW_DEFAULT_CONSTRUCTOR    = PLUGIN_ID + ".prefs.ShowDefaultConstructor";
	public static final String PREF_SHOW_METADATA               = PLUGIN_ID + ".prefs.ShowMetadata";
		
	// The shared instance
	private static SnowmanDecompilerPlugin plugin;
	
	
	/**
	 * The constructor
	 */
	public SnowmanDecompilerPlugin() {}
	
	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception 
	{
		super.start(context);
		plugin = this;

		// Setup ".class" file associations
		Display.getDefault().syncExec(new SetupClassFileAssociationRunnable());
	}

	/*
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception 
	{
		plugin.savePluginPreferences();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static SnowmanDecompilerPlugin getDefault() 
	{
		return plugin;
	}	

	protected static class SetupClassFileAssociationRunnable implements Runnable
	{
		public void run()
		{
			EditorRegistry registry = (EditorRegistry)
				WorkbenchPlugin.getDefault().getEditorRegistry();
			
			IFileEditorMapping[] mappings = registry.getFileEditorMappings();
			IFileEditorMapping c = null;
			IFileEditorMapping cws = null;
			
			// Search Class file editor mappings
			for (IFileEditorMapping mapping : mappings) 
			{
				if (mapping.getExtension().equals("class")) 
				{
					// ... Helios 3.6, Indigo 3.7, Juno 4.2, Kepler 4.3, ...
					c = mapping;
				}
				else if (mapping.getExtension().equals("class without source")) 
				{
					// Juno 4.2, Kepler 4.3, ...
					cws = mapping;
				}
			}

			if ((c != null) && (cws != null))
			{
				// Search JD editor descriptor on "class" extension
				for (IEditorDescriptor descriptor : c.getEditors())
				{		
					if (descriptor.getId().equals(EDITOR_ID))
					{
						// Remove JD editor on "class" extension
						//((FileEditorMapping)c).removeEditor((EditorDescriptor)descriptor);

						// Set JD as default editor on "class without source" extension
						registry.setDefaultEditor(
							"." + cws.getExtension(), descriptor.getId());
						break;
					}
				}
				
				// Restore the default editor for "class" extension
				IEditorDescriptor defaultClassFileEditor = 
					registry.findEditor(JavaUI.ID_CF_EDITOR);	
				if (defaultClassFileEditor != null)
				{
					registry.setDefaultEditor(
						"." + c.getExtension(), JavaUI.ID_CF_EDITOR);
				}				
				
				registry.setFileEditorMappings((FileEditorMapping[]) mappings);
				registry.saveAssociations();			
			}
		}
	}
}