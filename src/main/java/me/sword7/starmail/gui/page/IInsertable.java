package me.sword7.starmail.gui.page;

import java.util.List;

public interface IInsertable extends IPageContents {

    boolean isInsertable(int slot);

    List<Integer> getOrderedSlots();

}
