/*
 * Based off of source code by Emmanuel Dupuy
 * This program is made available under the terms of the GPLv3 License.
 */

package snowman.ide.eclipse.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import snowman.ide.eclipse.SnowmanDecompilerPlugin;

/**
 * PreferencePage
 * 
 * @project Snowman Decompiler Plug-In
 * @version 0.0.1
 */
public class PreferencePage
	extends FieldEditorPreferencePage implements IWorkbenchPreferencePage 
{
	public PreferencePage() 
	{
		super(SWT.NONE);
		setDescription("Snowman Decompiler preference page");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	@Override
	public void createFieldEditors() 
	{
		Composite fieldEditorParent = getFieldEditorParent();
		
		new Label(fieldEditorParent, SWT.NONE);

		addField(new BooleanFieldEditor(
			SnowmanDecompilerPlugin.PREF_ESCAPE_UNICODE_CHARACTERS, 
			"Escape unicode characters", fieldEditorParent));
		addField(new BooleanFieldEditor(
			SnowmanDecompilerPlugin.PREF_OMIT_PREFIX_THIS, 
			"Omit the prefix 'This' if possible", fieldEditorParent));
		addField(new BooleanFieldEditor(
			SnowmanDecompilerPlugin.PREF_REALIGN_LINE_NUMBERS, 
			"Realign line numbers", fieldEditorParent));
		addField(new BooleanFieldEditor(
			SnowmanDecompilerPlugin.PREF_SHOW_LINE_NUMBERS, 
			"Show original line numbers", fieldEditorParent));
		addField(new BooleanFieldEditor(
			SnowmanDecompilerPlugin.PREF_SHOW_DEFAULT_CONSTRUCTOR, 
			"Show default constructor", fieldEditorParent));
		addField(new BooleanFieldEditor(
			SnowmanDecompilerPlugin.PREF_SHOW_METADATA, 
			"Show metadata", fieldEditorParent));
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) 
	{
		setPreferenceStore(SnowmanDecompilerPlugin.getDefault().getPreferenceStore());		
	}
}