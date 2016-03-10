package com.sam_chordas.android.stockhawk.busEvents;

public class SymbolEvent {
    public enum STATE {SUCCESS, FAILURE}

    public final STATE state;

    public SymbolEvent(STATE state) {
        this.state = state;
    }
}
