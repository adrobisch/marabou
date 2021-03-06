/**
 * Marabou - Audio Tagger
 *
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 *
 * https://github.com/hennr/marabou
 *
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.model;

import com.google.common.eventbus.EventBus;
import com.mpatric.mp3agic.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ModelTest {

    @Test
    public void prefersId3v2TagsOverId3v1Tags() throws IOException {

        // given
        File fileMock = mock(File.class);
        when(fileMock.getCanonicalPath()).thenReturn("foo");

        final Mp3File mp3FileMock = mock(Mp3File.class);

        ID3v1Tag id3v1TagMock = mock(ID3v1Tag.class);
        when(mp3FileMock.hasId3v1Tag()).thenReturn(true);
        when(mp3FileMock.getId3v1Tag()).thenReturn(id3v1TagMock);

        ID3v2 id3v2TagMock = mock(ID3v2.class);
        when(mp3FileMock.hasId3v2Tag()).thenReturn(true);
        when(mp3FileMock.getId3v2Tag()).thenReturn(id3v2TagMock);

        Model model = new Model(new EventBus()) {
            protected Mp3File getMp3File(File audioFile) {
                return mp3FileMock;
            }
        };

        // when
        model.addFile(fileMock);

        // then
        verify(id3v2TagMock).getAlbum();
        verify(id3v2TagMock).getTrack();
        verify(id3v2TagMock).getArtist();
        verify(id3v2TagMock).getTitle();
        verify(id3v2TagMock).getYear();
        verify(id3v2TagMock).getGenre();
        verify(id3v2TagMock).getComment();
        verify(id3v2TagMock).getPartOfSet();
        verify(id3v2TagMock).getComposer();

        verifyZeroInteractions(id3v1TagMock);
    }

    @Test
    public void fallsBackToId3v1TagsOfNoId3v2TagsAreFound() throws IOException {

        // given
        File fileMock = mock(File.class);
        when(fileMock.getCanonicalPath()).thenReturn("foo");

        final Mp3File mp3FileMock = mock(Mp3File.class);

        ID3v1Tag id3v1TagMock = mock(ID3v1Tag.class);
        when(mp3FileMock.hasId3v1Tag()).thenReturn(true);
        when(mp3FileMock.getId3v1Tag()).thenReturn(id3v1TagMock);

        ID3v2 id3v2TagMock = mock(ID3v2.class);
        when(mp3FileMock.hasId3v2Tag()).thenReturn(false);
        when(mp3FileMock.getId3v2Tag()).thenReturn(id3v2TagMock);

        Model model = new Model(new EventBus()) {
            protected Mp3File getMp3File(File audioFile) {
                return mp3FileMock;
            }
        };

        // when
        model.addFile(fileMock);

        // then
        verify(id3v1TagMock).getAlbum();
        verify(id3v1TagMock).getTrack();
        verify(id3v1TagMock).getArtist();
        verify(id3v1TagMock).getTitle();
        verify(id3v1TagMock).getYear();
        verify(id3v1TagMock).getGenre();
        verify(id3v1TagMock).getComment();

        verifyZeroInteractions(id3v2TagMock);
    }

    @Test
    public void createsCorrectAudioFileIfNoTagsAreFound() throws IOException {

        // given
        File fileMock = mock(File.class);
        when(fileMock.getCanonicalPath()).thenReturn("foo");

        final Mp3File mp3FileMock = mock(Mp3File.class);

        when(mp3FileMock.hasId3v1Tag()).thenReturn(false);
        when(mp3FileMock.hasId3v2Tag()).thenReturn(false);

        Model model = new Model(new EventBus()) {
            protected Mp3File getMp3File(File audioFile) {
                return mp3FileMock;
            }
        };

        // when
        model.addFile(fileMock);
        AudioFile audioFile = model.getAudioFileByFilePath("foo");

        // then
        assertEquals("foo", audioFile.getFilePath());
        assertEquals("", audioFile.getAlbum());
        assertEquals("", audioFile.getArtist());
        assertEquals("0", audioFile.getBitRate());
        assertEquals("", audioFile.getChannels());
        assertEquals("", audioFile.getComment());
        assertEquals("", audioFile.getComposer());
        assertEquals("", audioFile.getDiscNumber());
        assertEquals("0:00", audioFile.getDuration());
        assertEquals("", audioFile.getGenre());
        assertEquals("0", audioFile.getSamplerate());
        assertEquals("", audioFile.getTitle());
        assertEquals("", audioFile.getTrack());
        assertEquals("", audioFile.getYear());
        assertEquals("mp3", audioFile.getEncoding());
    }

    @Test
    public void addFileCanHandleNastyReturnsInId3v1Tags() throws IOException {

        // given
        File fileMock = mock(File.class);
        when(fileMock.getCanonicalPath()).thenReturn("foo");

        final Mp3File mp3FileMock = mock(Mp3File.class);
        ID3v1Tag id3v1TagMock = mock(ID3v1Tag.class);
        when(mp3FileMock.hasId3v1Tag()).thenReturn(true);
        when(mp3FileMock.getId3v1Tag()).thenReturn(id3v1TagMock);

        when(id3v1TagMock.getArtist()).thenReturn(null);
        when(id3v1TagMock.getTitle()).thenReturn(null);
        when(id3v1TagMock.getAlbum()).thenReturn(null);
        when(id3v1TagMock.getTrack()).thenReturn(null);
        when(id3v1TagMock.getYear()).thenReturn(null);
        when(id3v1TagMock.getComment()).thenReturn(null);

        Model model = new Model(new EventBus()) {
            protected Mp3File getMp3File(File audioFile) {
                return mp3FileMock;
            }
        };

        // when
        model.addFile(fileMock);
        AudioFile audioFile = model.getAudioFileByFilePath("foo");

        // then
        assertEquals("", audioFile.getArtist());
        assertEquals("", audioFile.getTitle());
        assertEquals("", audioFile.getAlbum());
        assertEquals("", audioFile.getTrack());
        assertEquals("", audioFile.getYear());
        assertEquals("", audioFile.getComment());
    }

    @Test
    public void addFileCanHandleNastyReturnsInId3v2Tags() throws IOException {

        // given
        File fileMock = mock(File.class);
        when(fileMock.getCanonicalPath()).thenReturn("foo");

        final Mp3File mp3FileMock = mock(Mp3File.class);
        ID3v2 id3v2TagMock = mock(ID3v2.class);
        when(mp3FileMock.hasId3v2Tag()).thenReturn(true);
        when(mp3FileMock.getId3v2Tag()).thenReturn(id3v2TagMock);

        when(id3v2TagMock.getArtist()).thenReturn(null);
        when(id3v2TagMock.getTitle()).thenReturn(null);
        when(id3v2TagMock.getAlbum()).thenReturn(null);
        when(id3v2TagMock.getTrack()).thenReturn(null);
        when(id3v2TagMock.getYear()).thenReturn(null);
        when(id3v2TagMock.getComment()).thenReturn(null);

        Model model = new Model(new EventBus()) {
            protected Mp3File getMp3File(File audioFile) {
                return mp3FileMock;
            }
        };

        // when
        model.addFile(fileMock);
        AudioFile audioFile = model.getAudioFileByFilePath("foo");

        // then
        assertEquals("", audioFile.getArtist());
        assertEquals("", audioFile.getTitle());
        assertEquals("", audioFile.getAlbum());
        assertEquals("", audioFile.getTrack());
        assertEquals("", audioFile.getYear());
        assertEquals("", audioFile.getComment());
        assertEquals("", audioFile.getChannels());
    }

    @Test
    public void getAudioFileByFilePath() throws Exception {

        // given
        final Mp3File mp3FileMock = mock(Mp3File.class);

        Model model = new Model(new EventBus()) {
            protected Mp3File getMp3File(File audioFile) {
              return mp3FileMock;
            }
        };

        File audioFile = mock(File.class);
        when(audioFile.getCanonicalPath()).thenReturn("foo");

        // when
        model.addFile(audioFile);
        AudioFile result = model.getAudioFileByFilePath("foo");

        // then
        assertNotNull(result);
    }

    @Test
    public void transfersAllMp3FileFieldsIntoTheModel() throws IOException {

        // given
        ID3v24Tag id3v24TagMock = mock(ID3v24Tag.class);
        when(id3v24TagMock.getAlbum()).thenReturn("album");
        when(id3v24TagMock.getTrack()).thenReturn("track");
        when(id3v24TagMock.getArtist()).thenReturn("artist");
        when(id3v24TagMock.getComment()).thenReturn("comment");
        when(id3v24TagMock.getComposer()).thenReturn("composer");
        when(id3v24TagMock.getPartOfSet()).thenReturn("disc number");
        when(id3v24TagMock.getGenre()).thenReturn(22);
        when(id3v24TagMock.getTitle()).thenReturn("title");
        when(id3v24TagMock.getYear()).thenReturn("year");

        final Mp3File mp3FileMock = mock(Mp3File.class);
        when(mp3FileMock.hasId3v2Tag()).thenReturn(true);
        when(mp3FileMock.getId3v2Tag()).thenReturn(id3v24TagMock);
        when(mp3FileMock.getBitrate()).thenReturn(666);
        when(mp3FileMock.getChannelMode()).thenReturn("channel mode");
        when(mp3FileMock.getLengthInSeconds()).thenReturn(61l);
        when(mp3FileMock.getSampleRate()).thenReturn(44000);

        Model model = new Model(new EventBus()) {
            protected Mp3File getMp3File(File audioFile) {
                return mp3FileMock;
            }
        };

        File inputFile = mock(File.class);
        when(inputFile.getCanonicalPath()).thenReturn("foo");

        // when
        model.addFile(inputFile);
        AudioFile audioFile = model.getAudioFileByFilePath("foo");

        // then
        assertEquals("album", audioFile.getAlbum());
        assertEquals("track", audioFile.getTrack());
        assertEquals("artist", audioFile.getArtist());
        assertEquals("comment", audioFile.getComment());
        assertEquals("composer", audioFile.getComposer());
        assertEquals("666", audioFile.getBitRate());
        assertEquals("channel mode", audioFile.getChannels());
        assertEquals("disc number", audioFile.getDiscNumber());
        assertEquals("mp3", audioFile.getEncoding());
        assertEquals("1:01", audioFile.getDuration());
        assertEquals("foo", audioFile.getFilePath());
        assertEquals("Death Metal", audioFile.getGenre());
        assertEquals("44000", audioFile.getSamplerate());
        assertEquals("title", audioFile.getTitle());
        assertEquals("year", audioFile.getYear());
    }

    @Test
    public void calculatesTrackLengthAsExpected() {

        // given
        Model model = new Model(new EventBus());

        // expect
        assertEquals("0:00", model.calculateTrackLength(-1l));
        assertEquals("0:00", model.calculateTrackLength(0l));
        assertEquals("0:59", model.calculateTrackLength(59l));
        assertEquals("1:00", model.calculateTrackLength(60l));
        assertEquals("1:01", model.calculateTrackLength(61l));
        assertEquals("10:00", model.calculateTrackLength(600l));
        assertEquals("99:00", model.calculateTrackLength(5940l));
        assertEquals("100:01", model.calculateTrackLength(6001l));
    }
}
