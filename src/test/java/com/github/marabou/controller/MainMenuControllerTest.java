package com.github.marabou.controller;

import com.github.marabou.audio.AudioFileFilter;
import com.github.marabou.db.HSQLDBController;
import com.github.marabou.gui.AboutWindow;
import com.github.marabou.properties.UserProperties;
import com.github.marabou.service.AudioFileService;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MainMenuControllerTest {

    @Test
    public void opensValidFile() throws Exception {

        // given
        MainMenuController controllerUnderTest = givenAMainMenuControllerWithMocks();

        HSQLDBController hsqldbControllerMock = mock(HSQLDBController.class);
        controllerUnderTest.hsqldbController = hsqldbControllerMock;

        File file = aValidMockedFile();

        // when
        controllerUnderTest.openFile(file);

        // then
        verify(hsqldbControllerMock).insertFile(file);
        verify(hsqldbControllerMock).addAllTableItems();
    }

    @Test
    public void testOpenFiles() throws Exception {

        // given
        MainMenuController controllerUnderTest = givenAMainMenuControllerWithMocks();

        HSQLDBController hsqldbControllerMock = mock(HSQLDBController.class);
        controllerUnderTest.hsqldbController = hsqldbControllerMock;

        List<File> files = new ArrayList<>(3);
        files.add(aValidMockedFile());
        files.add(aValidMockedFile());

        // when
        controllerUnderTest.openFiles(files);

        // then
        verify(hsqldbControllerMock).insertFile(files.get(0));
        verify(hsqldbControllerMock).insertFile(files.get(1));
        verify(hsqldbControllerMock, times(2)).addAllTableItems();
    }

    private MainMenuController givenAMainMenuControllerWithMocks() {
        AudioFileFilter audioFileFilterMock = mock(AudioFileFilter.class);
        when(audioFileFilterMock.accept(any(File.class))).thenReturn(true);
        UserProperties userPropertiesMock = mock(UserProperties.class);
        Shell shellMock = mock(Shell.class);
        AudioFileService audioFileServiceMock = mock(AudioFileService.class);
        AboutWindow aboutWindowMock = mock(AboutWindow.class);

        return new MainMenuController(audioFileFilterMock, shellMock, userPropertiesMock, audioFileServiceMock, aboutWindowMock);
    }

    private File aValidMockedFile() {
        File file = mock(File.class);
        when(file.getName()).thenReturn("foo.mp3");
        when(file.canRead()).thenReturn(true);
        when(file.isDirectory()).thenReturn(false);
        return file;
    }
}
