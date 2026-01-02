package com.dh.app.core.patternlockview.listener;

import com.dh.app.core.patternlockview.PatternLockView;

import java.util.List;


public interface PatternLockViewListener {


    void onStarted();


    void onProgress(List<PatternLockView.Dot> progressPattern);

    void onComplete(List<PatternLockView.Dot> pattern);


    void onCleared();
}
