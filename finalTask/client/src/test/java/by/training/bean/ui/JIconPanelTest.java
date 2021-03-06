package by.training.bean.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import by.training.ui.JIconPanel;

public class JIconPanelTest {

    @Test
    public void testGetNumberIcon() {
        JIconPanel mockPanel = mock(JIconPanel.class);
        when(mockPanel.getIndex()).thenReturn(5);
        assertEquals(mockPanel.getIndex(), 5);
    }

    @Test(expected = NullPointerException.class)
    public void testSetOptions() {
        JIconPanel mockPanel = mock(JIconPanel.class);
        doThrow(NullPointerException.class).when(mockPanel).titleChanged();

        JIconPanel iconPanel = new JIconPanel(null);
        iconPanel.titleChanged();
    }

}
