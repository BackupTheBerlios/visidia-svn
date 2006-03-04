package visidia.gui.presentation;

import java.lang.NoSuchMethodException;
import visidia.gui.presentation.*;
import visidia.gui.presentation.userInterfaceEdition.undo.*;

/*  Those 3 classes are used by SelectUnit and as they can change,
 *  a SelectUnit instance must be able to get them using those functions. */
public interface SelectionGetData {

    public SelectionDessin getSelectionDessin ();

    /**
     *  Throws noSuchMethoException if the undo processing isn't used
     */
    public UndoInfo getUndoInfo ()
	throws NoSuchMethodException;

    public RecoverableObject getRecoverableObject ();

}
