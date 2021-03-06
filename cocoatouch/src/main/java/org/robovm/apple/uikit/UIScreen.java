/*
 * Copyright (C) 2014 Trillian Mobile AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.robovm.apple.uikit;

/*<imports>*/
import java.io.*;
import java.nio.*;
import java.util.*;
import org.robovm.objc.*;
import org.robovm.objc.annotation.*;
import org.robovm.objc.block.*;
import org.robovm.rt.*;
import org.robovm.rt.bro.*;
import org.robovm.rt.bro.annotation.*;
import org.robovm.rt.bro.ptr.*;
import org.robovm.apple.foundation.*;
import org.robovm.apple.coreanimation.*;
import org.robovm.apple.coregraphics.*;
import org.robovm.apple.coredata.*;
import org.robovm.apple.coreimage.*;
/*</imports>*/

/*<javadoc>*/
/**
 * @since Available in iOS 2.0 and later.
 */
/*</javadoc>*/
/*<annotations>*/@Library("UIKit") @NativeClass/*</annotations>*/
/*<visibility>*/public/*</visibility>*/ class /*<name>*/UIScreen/*</name>*/ 
    extends /*<extends>*/NSObject/*</extends>*/ 
    /*<implements>*//*</implements>*/ {

    /*<ptr>*/public static class UIScreenPtr extends Ptr<UIScreen, UIScreenPtr> {}/*</ptr>*/
    /*<bind>*/static { ObjCRuntime.bind(UIScreen.class); }/*</bind>*/
    /*<constants>*//*</constants>*/
    /*<constructors>*/
    public UIScreen() {}
    protected UIScreen(SkipInit skipInit) { super(skipInit); }
    /*</constructors>*/
    /*<properties>*/
    @Property(selector = "bounds")
    public native @ByVal CGRect getBounds();
    @Property(selector = "applicationFrame")
    public native @ByVal CGRect getApplicationFrame();
    @Property(selector = "scale")
    public native @MachineSizedFloat double getScale();
    @Property(selector = "availableModes")
    public native NSArray<UIScreenMode> getAvailableModes();
    @Property(selector = "preferredMode")
    public native UIScreenMode getPreferredMode();
    @Property(selector = "currentMode")
    public native UIScreenMode getCurrentMode();
    @Property(selector = "setCurrentMode:")
    public native void setCurrentMode(UIScreenMode v);
    @Property(selector = "overscanCompensation")
    public native UIScreenOverscanCompensation getOverscanCompensation();
    @Property(selector = "setOverscanCompensation:")
    public native void setOverscanCompensation(UIScreenOverscanCompensation v);
    @Property(selector = "mirroredScreen")
    public native UIScreen getMirroredScreen();
    @Property(selector = "brightness")
    public native @MachineSizedFloat double getBrightness();
    @Property(selector = "setBrightness:")
    public native void setBrightness(@MachineSizedFloat double v);
    @Property(selector = "wantsSoftwareDimming")
    public native boolean isWantsSoftwareDimming();
    @Property(selector = "setWantsSoftwareDimming:")
    public native void setWantsSoftwareDimming(boolean v);
    /*</properties>*/
    /*<members>*//*</members>*/
    /*<methods>*/
    /**
     * @since Available in iOS 4.0 and later.
     */
    @Method(selector = "displayLinkWithTarget:selector:")
    public native CADisplayLink createDisplayLink(NSObject target, Selector sel);
    /**
     * @since Available in iOS 3.2 and later.
     */
    @Method(selector = "screens")
    public static native NSArray<UIScreen> getScreens();
    @Method(selector = "mainScreen")
    public static native UIScreen getMainScreen();
    /**
     * @since Available in iOS 7.0 and later.
     */
    @Method(selector = "snapshotViewAfterScreenUpdates:")
    public native UIView snapshotView(boolean afterUpdates);
    /*</methods>*/
}
