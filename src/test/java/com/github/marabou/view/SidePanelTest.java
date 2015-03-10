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
package com.github.marabou.view;

import com.github.marabou.model.AudioFile;
import com.github.marabou.view.SidePanel.ComboAndLabelNames;
import com.google.common.collect.Sets;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SidePanelTest {

    @Test
    public void holdsAllGivenEntriesInTheCombos() {

        // given
        SidePanel sidePanel = aSidePanelWithSwtMocked();
        AudioFile audioFile = aValidCompleteAudioFile();
        AudioFile anotherAudioFile = anotherValidCompleteAudioFile();

        // when
        sidePanel.updateLists(Sets.newHashSet(audioFile, anotherAudioFile));

        // then
        assertEquals(2, sidePanel.comboBoxes.get(ComboAndLabelNames.Artist).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Artist).getItems()).contains(audioFile.getArtist()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Artist).getItems()).contains(anotherAudioFile.getArtist()));

        assertEquals(2, sidePanel.comboBoxes.get(ComboAndLabelNames.Title).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Title).getItems()).contains(audioFile.getTitle()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Title).getItems()).contains(anotherAudioFile.getTitle()));

        assertEquals(2, sidePanel.comboBoxes.get(ComboAndLabelNames.Album).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Album).getItems()).contains(audioFile.getAlbum()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Album).getItems()).contains(anotherAudioFile.getAlbum()));

        assertEquals(2, sidePanel.comboBoxes.get(ComboAndLabelNames.Track).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Track).getItems()).contains(audioFile.getTrack()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Track).getItems()).contains(anotherAudioFile.getTrack()));

        assertEquals(2, sidePanel.comboBoxes.get(ComboAndLabelNames.Year).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Year).getItems()).contains(audioFile.getYear()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Year).getItems()).contains(anotherAudioFile.getYear()));

        assertEquals(2, sidePanel.comboBoxes.get(ComboAndLabelNames.Genre).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Genre).getItems()).contains(audioFile.getGenre()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Genre).getItems()).contains(anotherAudioFile.getGenre()));

        assertEquals(2, sidePanel.comboBoxes.get(ComboAndLabelNames.Comments).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Comments).getItems()).contains(audioFile.getComment()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Comments).getItems()).contains(anotherAudioFile.getComment()));

        assertEquals(2, sidePanel.comboBoxes.get(ComboAndLabelNames.Disc_number).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Disc_number).getItems()).contains(audioFile.getDiscNumber()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Disc_number).getItems()).contains(anotherAudioFile.getDiscNumber()));

        assertEquals(2, sidePanel.comboBoxes.get(ComboAndLabelNames.Composer).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Composer).getItems()).contains(audioFile.getComposer()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(ComboAndLabelNames.Composer).getItems()).contains(anotherAudioFile.getComposer()));
    }

    @Test
    public void doesNotHoldOldEntriesAfterUpdateGetsCalled() {

        // given
        SidePanel sidePanel = aSidePanelWithSwtMocked();

        // when
        AudioFile audioFile = aValidCompleteAudioFile();
        sidePanel.updateLists(Sets.newHashSet(audioFile));

        //
        AudioFile secondAudioFile = anotherValidCompleteAudioFile();
        sidePanel.updateLists(Sets.newHashSet(secondAudioFile));

        // then
        for (ComboAndLabelNames name : ComboAndLabelNames.values()) {
            assertEquals(1, sidePanel.comboBoxes.get(name).getItemCount());
        }

        assertEquals(secondAudioFile.getArtist(), sidePanel.comboBoxes.get(ComboAndLabelNames.Artist).getText());
        assertEquals(secondAudioFile.getTitle(), sidePanel.comboBoxes.get(ComboAndLabelNames.Title).getText());
        assertEquals(secondAudioFile.getAlbum(), sidePanel.comboBoxes.get(ComboAndLabelNames.Album).getText());
        assertEquals(secondAudioFile.getTrack(), sidePanel.comboBoxes.get(ComboAndLabelNames.Track).getText());
        assertEquals(secondAudioFile.getYear(), sidePanel.comboBoxes.get(ComboAndLabelNames.Year).getText());
        assertEquals(secondAudioFile.getGenre(), sidePanel.comboBoxes.get(ComboAndLabelNames.Genre).getText());
        assertEquals(secondAudioFile.getComment(), sidePanel.comboBoxes.get(ComboAndLabelNames.Comments).getText());
        assertEquals(secondAudioFile.getDiscNumber(), sidePanel.comboBoxes.get(ComboAndLabelNames.Disc_number).getText());
        assertEquals(secondAudioFile.getComposer(), sidePanel.comboBoxes.get(ComboAndLabelNames.Composer).getText());
    }

    private SidePanel aSidePanelWithSwtMocked() {
        SashForm sashFormMock = new SashForm(new Shell(), 0);
        return new SidePanel(sashFormMock);
    }

    private AudioFile aValidCompleteAudioFile() {
        return new AudioFile("filePath")
                    .withArtist("Slayer")
                    .withTitle("Angel of death")
                    .withAlbum("Reign in Blood")
                    .withTrack("1")
                    .withYear("1986")
                    .withGenre("Thrash Metal")
                    .withComment("Slayer rules")
                    .withDiscNumber("1")
                    .withComposer("Jeff Hanneman");
    }

    private AudioFile anotherValidCompleteAudioFile() {
        return new AudioFile("filePath")
                .withArtist("Britney Spears")
                .withTitle("Oops! … I Did It Again")
                .withAlbum("Oops! … I Did It Again")
                .withTrack("1")
                .withYear("2000")
                .withGenre("Pop")
                .withComment("Oh man")
                .withDiscNumber("1")
                .withComposer("Who cares?");
    }
}