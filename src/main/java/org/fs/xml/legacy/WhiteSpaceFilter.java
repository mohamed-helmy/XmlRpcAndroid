package org.fs.xml.legacy;

import javax.xml.stream.EventFilter;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

/**
 * Created by Fatih on 24/06/16.
 */
class WhiteSpaceFilter implements EventFilter {

//cleans white space
    @Override public boolean accept(XMLEvent event) {
        return !(event.isCharacters() && ((Characters) event).isWhiteSpace());
    }
}
