/*
 * Based off of source code by Emmanuel Dupuy
 * This program is made available under the terms of the GPLv3 License.
 */

package snowman.ide.eclipse.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import snowman.ide.eclipse.SnowmanDecompilerPlugin;

/**
 * Class used to initialize default preference values.
 * 
 * @project Snowman Decompiler Plug-In
 * @version 0.0.1
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer 
{
	/**
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() 
	{
		IPreferenceStore store = SnowmanDecompilerPlugin.getDefault().getPreferenceStore();
		store.setDefault(SnowmanDecompilerPlugin.PREF_ESCAPE_UNICODE_CHARACTERS, false);
		store.setDefault(SnowmanDecompilerPlugin.PREF_OMIT_PREFIX_THIS, false);
		store.setDefault(SnowmanDecompilerPlugin.PREF_REALIGN_LINE_NUMBERS, true);
		store.setDefault(SnowmanDecompilerPlugin.PREF_SHOW_LINE_NUMBERS, true);
		store.setDefault(SnowmanDecompilerPlugin.PREF_SHOW_DEFAULT_CONSTRUCTOR, false);
		store.setDefault(SnowmanDecompilerPlugin.PREF_SHOW_METADATA, true);
	}
}
