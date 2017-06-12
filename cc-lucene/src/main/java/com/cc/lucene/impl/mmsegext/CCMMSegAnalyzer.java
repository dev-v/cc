package com.cc.lucene.impl.mmsegext;

import java.io.Reader;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;

/**
 * @author wenlongchen
 * @since Oct 27, 2016
 */
public class CCMMSegAnalyzer  extends MMSegAnalyzer {

  @Override
  protected TokenStreamComponents createComponents(String fieldName,
      Reader reader) {
    return new TokenStreamComponents(new CCMMSegTokenizer(newSeg(), reader));
  }

}
