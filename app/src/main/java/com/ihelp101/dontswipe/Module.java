package com.ihelp101.dontswipe;

import android.app.AndroidAppHelper;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class Module implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    Context nContext;
    Context mContext;
    int Hide = 0;
    String packageName;
    XSharedPreferences cPreferences;
    XSharedPreferences gPreferences;
    XSharedPreferences mPreferences;
    XSharedPreferences pPreferences;

    //Hooks//
    String CyanogenFullScreen;
    String CyanogenFullScreenMenu;
    String CyanogenFullScreenMenuClick;
    String CyanogenViewPager;
    String CyanogenViewPagerMotion;
    String GoogleSlide;
    String GoogleSlideTouch;
    String GoogleAction;
    String GoogleActionMenu;
    String GoogleActionMenuClick;
    String PiktureMenu;
    String PiktureMenuMenu;
    String PiktureMenuClick;
    String PiktureMenuClickMenuClick;
    String PiktureMotion;
    String PiktureMotionMotion;
    //Hooks//

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        cPreferences = new XSharedPreferences("com.ihelp101.dontswipe", "Cyanogen");
        gPreferences = new XSharedPreferences("com.ihelp101.dontswipe", "Google");
        mPreferences = new XSharedPreferences("com.ihelp101.dontswipe", "Prefs");
        pPreferences = new XSharedPreferences("com.ihelp101.dontswipe", "Pikture");
    }

    private static void log(String log) {
		XposedBridge.log("Dont Swipe: " + log);
	}

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {

        if (lpparam.packageName.equals("com.android.gallery3d")) {
            // Thank you to KeepChat For the Following Code Snippet
            // http://git.io/JJZPaw
            Object activityThread = callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread");
            final Context context = (Context) callMethod(activityThread, "getSystemContext");

            final int versionCheck = context.getPackageManager().getPackageInfo(lpparam.packageName, 0).versionCode;
            //End Snippet

            nContext = context;

            log("Gallery Version Code: " + versionCheck);
            hookGallery3D(lpparam);
        }

        if (lpparam.packageName.equals("com.google.android.apps.photos")) {
            // Thank you to KeepChat For the Following Code Snippet
            // http://git.io/JJZPaw
            Object activityThread = callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread");
            final Context context = (Context) callMethod(activityThread, "getSystemContext");

            final int versionCheck = context.getPackageManager().getPackageInfo(lpparam.packageName, 0).versionCode;
            //End Snippet

            nContext = context;

            log("Google Photos Version Code: " + versionCheck);

            gPreferences.reload();

            try {
                String[] Google = gPreferences.getString("Hooks", "Default").split(";");
                GoogleSlide = Google[1];
                GoogleSlideTouch = Google[2];
                GoogleAction = Google[3];
                GoogleActionMenu = Google[4];
                GoogleActionMenuClick = Google[5];
            } catch (Exception e) {
                log("Please update your hooks.");
            }

            hookGooglePhotos(lpparam);
        }

        if (lpparam.packageName.equals("com.diune.pictures")) {
            // Thank you to KeepChat For the Following Code Snippet
            // http://git.io/JJZPaw
            Object activityThread = callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread");
            final Context context = (Context) callMethod(activityThread, "getSystemContext");

            final int versionCheck = context.getPackageManager().getPackageInfo(lpparam.packageName, 0).versionCode;
            //End Snippet

            nContext = context;

            log("Pikture Version Code: " + versionCheck);

            pPreferences.reload();

            try {
                String[] Pikture = pPreferences.getString("Hooks", "Default").split(";");
                PiktureMenu = Pikture[1];
                PiktureMenuMenu = Pikture[2];
                PiktureMenuClick = Pikture[3];
                PiktureMenuClickMenuClick = Pikture[4];
                PiktureMotion = Pikture[5];
                PiktureMotionMotion = Pikture[6];
            } catch (Exception e) {
                log("Please update your hooks.");
            }

            hookPiktures(lpparam);
        }

        if (lpparam.packageName.equals("com.alensw.PicFolder")) {
            // Thank you to KeepChat For the Following Code Snippet
            // http://git.io/JJZPaw
            Object activityThread = callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread");
            final Context context = (Context) callMethod(activityThread, "getSystemContext");

            final int versionCheck = context.getPackageManager().getPackageInfo(lpparam.packageName, 0).versionCode;
            //End Snippet

            nContext = context;

            log("QuickPic Version Code: " + versionCheck);
            hookQuickPic(lpparam);
        }

        if (lpparam.packageName.equals("com.cyngn.gallerynext")) {
            // Thank you to KeepChat For the Following Code Snippet
            // http://git.io/JJZPaw
            Object activityThread = callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread");
            final Context context = (Context) callMethod(activityThread, "getSystemContext");

            final int versionCheck = context.getPackageManager().getPackageInfo(lpparam.packageName, 0).versionCode;
            //End Snippet

            nContext = context;

            log("Cyanogen Gallery Version Code: " + versionCheck);

            cPreferences.reload();

            try {
                String[] Cyanogen = cPreferences.getString("Hooks", "Default").split(";");
                CyanogenFullScreen = Cyanogen[1];
                CyanogenFullScreenMenu = Cyanogen[2];
                CyanogenFullScreenMenuClick = Cyanogen[3];
                CyanogenViewPager = Cyanogen[4];
                CyanogenViewPagerMotion = Cyanogen[5];
            } catch (Exception e) {
                log("Please update your hooks.");
            }


            hookCyanogen(lpparam);
        }
	}

    private void hookCyanogen (final LoadPackageParam lpparam) {
        final Class<?> FullScreen = findClass(CyanogenFullScreen, lpparam.classLoader);
        final Class<?> ViewPager = findClass(CyanogenViewPager, lpparam.classLoader);

        XposedHelpers.findAndHookMethod(FullScreen, CyanogenFullScreenMenu, int.class, Menu.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Menu menu = (Menu) param.args[1];
                try {
                    int menuSize = menu.size() - 1;
                    if (!menu.getItem(menuSize).toString().equals(ResourceHelper.getString(nContext, R.string.lock)) && !menu.getItem(menuSize).toString().equals(ResourceHelper.getString(nContext, R.string.unlock))) {
                        if (Hide == 0) {
                            menu.add(ResourceHelper.getString(nContext, R.string.lock));
                        } else {
                            menu.add(ResourceHelper.getString(nContext, R.string.unlock));
                        }
                    }
                } catch (Exception e) {

                }
            }
        });

        XposedHelpers.findAndHookMethod(FullScreen, CyanogenFullScreenMenuClick, MenuItem.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                mContext = AndroidAppHelper.currentApplication().getApplicationContext();

                MenuItem menuItem = (MenuItem) param.args[0];
                if (menuItem.getTitle().toString().equals(ResourceHelper.getString(nContext, R.string.lock))) {
                    Hide = 1;
                    menuItem.setTitle(ResourceHelper.getString(nContext, R.string.unlock));
                } else {
                    if (menuItem.getTitle().toString().equals(ResourceHelper.getString(nContext, R.string.unlock))) {
                        Hide = 0;
                        menuItem.setTitle(ResourceHelper.getString(nContext, R.string.lock));
                    }
                }
            }
        });

        XposedHelpers.findAndHookMethod(ViewPager, CyanogenViewPagerMotion, MotionEvent.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                MotionEvent motionEvent = (MotionEvent) param.args[0];
                if (motionEvent.getAction() == 2 && Hide == 1) {
                    mPreferences.reload();
                    String choice = mPreferences.getString("Text", "Default");

                    if (choice.equals("Crash")) {
                        packageName = lpparam.packageName;
                        Respect();
                        param.setResult(null);
                    } else {
                        packageName = lpparam.packageName;
                        Respect();
                    }
                }
            }
        });
    }

    private void hookGallery3D (final LoadPackageParam lpparam) {
        final Class<?> View = findClass("com.android.gallery3d.ui.PhotoView", lpparam.classLoader);
        final Class<?> Activity = findClass("com.android.gallery3d.app.AbstractGalleryActivity", lpparam.classLoader);


        XposedHelpers.findAndHookMethod(View, "slideToNextPicture", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (Hide == 1) {
                    mPreferences.reload();
                    mContext = AndroidAppHelper.currentApplication().getApplicationContext();
                    String choice = mPreferences.getString("Text", "Default");

                    if (choice.equals("Crash")) {
                        packageName = lpparam.packageName;
                        Respect();
                        param.setResult(null);
                    } else {
                        packageName = lpparam.packageName;
                        Respect();
                    }
                }
            }
        });

        XposedHelpers.findAndHookMethod(View, "slideToPrevPicture", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (Hide == 1) {
                    mPreferences.reload();
                    mContext = AndroidAppHelper.currentApplication().getApplicationContext();
                    String choice = mPreferences.getString("Text", "Default");

                    if (choice.equals("Crash")) {
                        packageName = lpparam.packageName;
                        Respect();
                        param.setResult(null);
                    } else {
                        packageName = lpparam.packageName;
                        Respect();
                    }
                }
            }
        });

        XposedHelpers.findAndHookMethod(Activity, "onCreateOptionsMenu", Menu.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                Menu menu = (Menu) param.args[0];
                if (menu.getItem(1).toString().equals("Share panorama")) {
                    if (Hide == 0) {
                        menu.add(ResourceHelper.getString(nContext, R.string.lock));
                    } else {
                        menu.add(ResourceHelper.getString(nContext, R.string.unlock));
                    }
                }
            }
        });

        XposedHelpers.findAndHookMethod(Activity, "onOptionsItemSelected", MenuItem.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                MenuItem menuItem = (MenuItem) param.args[0];
                if (menuItem.getTitle().toString().equals(ResourceHelper.getString(nContext, R.string.lock))) {
                    Hide = 1;
                    menuItem.setTitle(ResourceHelper.getString(nContext, R.string.unlock));
                } else {
                    if (menuItem.getTitle().toString().equals(ResourceHelper.getString(nContext, R.string.unlock))) {
                        Hide = 0;
                        menuItem.setTitle(ResourceHelper.getString(nContext, R.string.lock));
                    }
                }
            }
        });
    }

    private void hookGooglePhotos(final LoadPackageParam lpparam) {
        final Class<?> Slide = findClass(GoogleSlide, lpparam.classLoader);
        final Class<?> Action = findClass(GoogleAction, lpparam.classLoader);

        XposedHelpers.findAndHookMethod(Slide, GoogleSlideTouch, MotionEvent.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                MotionEvent motionEvent = (MotionEvent) param.args[0];
                if (motionEvent.getAction() == 2 && Hide == 1) {
                    mPreferences.reload();
                    String choice = mPreferences.getString("Text", "Default");

                    if (choice.equals("Crash")) {
                        packageName = lpparam.packageName;
                        Respect();
                        param.setResult(null);
                    } else {
                        packageName = lpparam.packageName;
                        Respect();
                    }
                }
            }
        });

        XposedHelpers.findAndHookMethod(Action, GoogleActionMenu, Menu.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                mContext = AndroidAppHelper.currentApplication().getApplicationContext();

                Menu menu = (Menu) param.args[0];
                if (menu.getItem(1).toString().equals("Loop video")) {
                    if (Hide == 0) {
                        menu.add(ResourceHelper.getString(nContext, R.string.lock));
                    } else {
                        menu.add(ResourceHelper.getString(nContext, R.string.unlock));
                    }
                }
            }
        });

        XposedHelpers.findAndHookMethod(Action, GoogleActionMenuClick, MenuItem.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                MenuItem menuItem = (MenuItem) param.args[0];
                try {
                    if (menuItem.getTitle().toString().equals(ResourceHelper.getString(nContext, R.string.lock))) {
                        Hide = 1;
                        menuItem.setTitle(ResourceHelper.getString(nContext, R.string.unlock));
                    } else {
                        if (menuItem.getTitle().toString().equals(ResourceHelper.getString(nContext, R.string.unlock))) {
                            Hide = 0;
                            menuItem.setTitle(ResourceHelper.getString(nContext, R.string.lock));
                        }
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void hookPiktures (final LoadPackageParam lpparam) {
        final Class<?> Menu = findClass(PiktureMenu, lpparam.classLoader);
        final Class<?> MenuClick = findClass(PiktureMenuClick, lpparam.classLoader);
        final Class<?> Motion = findClass(PiktureMotion, lpparam.classLoader);

        XposedHelpers.findAndHookMethod(Menu, PiktureMenuMenu, Menu.class, int.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                Menu menu = (Menu) param.args[0];
                if (menu.getItem(1).toString().equals("Rotate left")) {
                    int menuSize = menu.size() - 1;
                    if (!menu.getItem(menuSize).toString().equals(ResourceHelper.getString(nContext, R.string.lock)) && !menu.getItem(menuSize).toString().equals(ResourceHelper.getString(nContext, R.string.unlock))) {
                        if (Hide == 0) {
                            menu.add(ResourceHelper.getString(nContext, R.string.lock));
                        } else {
                            menu.add(ResourceHelper.getString(nContext, R.string.unlock));
                        }
                    }
                }
            }
        });

        XposedHelpers.findAndHookMethod(MenuClick, PiktureMenuClickMenuClick, MenuItem.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                mContext = AndroidAppHelper.currentApplication().getApplicationContext();

                MenuItem menuItem = (MenuItem) param.args[0];
                if (menuItem.getTitle().toString().equals(ResourceHelper.getString(nContext, R.string.lock))) {
                    Hide = 1;
                    menuItem.setTitle(ResourceHelper.getString(nContext, R.string.unlock));
                } else {
                    if (menuItem.getTitle().toString().equals(ResourceHelper.getString(nContext, R.string.unlock))) {
                        Hide = 0;
                        menuItem.setTitle(ResourceHelper.getString(nContext, R.string.lock));
                    }
                }
            }
        });

        XposedHelpers.findAndHookMethod(Motion, PiktureMotionMotion, MotionEvent.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                mPreferences.reload();
                String choice = mPreferences.getString("Text", "Default");

                if (choice.equals("Crash")) {
                    packageName = lpparam.packageName;
                    Respect();
                    param.setResult(null);
                } else {
                    MotionEvent motionEvent = (MotionEvent) param.args[0];
                    if (motionEvent.getAction() == 2 && Hide == 1) {
                        packageName = lpparam.packageName;
                        Respect();
                    }
                }
            }
        });
    }

    private void hookQuickPic (final LoadPackageParam lpparam) {
        final Class<?> Menu = findClass("com.alensw.ui.c.cx", lpparam.classLoader);
        final Class<?> View = findClass("com.alensw.ui.view.PictureView", lpparam.classLoader);

        XposedHelpers.findAndHookMethod(Menu, "onCreateOptionsMenu", Menu.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Menu menu = (Menu) param.args[0];
                try {
                    int menuSize = menu.size() - 1;
                    if (menu.getItem(1).toString().equals("Share") && !menu.getItem(menuSize).toString().equals(ResourceHelper.getString(nContext, R.string.lock)) && !menu.getItem(menuSize).toString().equals(ResourceHelper.getString(nContext, R.string.unlock))) {
                        if (Hide == 0) {
                            menu.add(ResourceHelper.getString(nContext, R.string.lock));
                        } else {
                            menu.add(ResourceHelper.getString(nContext, R.string.unlock));
                        }
                    }
                } catch (Exception e){

                }
            }
        });

        XposedHelpers.findAndHookMethod(Menu, "onOptionsItemSelected", MenuItem.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                mContext = AndroidAppHelper.currentApplication().getApplicationContext();

                MenuItem menuItem = (MenuItem) param.args[0];
                if (menuItem.getTitle().toString().equals(ResourceHelper.getString(nContext, R.string.lock))) {
                    Hide = 1;
                    menuItem.setTitle(ResourceHelper.getString(nContext, R.string.unlock));
                } else {
                    if (menuItem.getTitle().toString().equals(ResourceHelper.getString(nContext, R.string.unlock))) {
                        Hide = 0;
                        menuItem.setTitle(ResourceHelper.getString(nContext, R.string.lock));
                    }
                }
            }
        });

        XposedHelpers.findAndHookMethod(View, "onTouchEvent", MotionEvent.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                MotionEvent motionEvent = (MotionEvent) param.args[0];
                if (motionEvent.getAction() == 2 && Hide == 1) {
                    mPreferences.reload();
                    String choice = mPreferences.getString("Text", "Default");

                    if (choice.equals("Crash")) {
                        packageName = lpparam.packageName;
                        Respect();
                        param.setResult(null);
                    } else {
                        packageName = lpparam.packageName;
                        Respect();
                    }
                }
            }
        });
    }

    public void Respect() {
        mPreferences.reload();

        String choice = mPreferences.getString("Text", "Default");

        try {
            if (choice.equals("Home") || choice.equals("Crash")) {
                Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(launchIntent);

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            } else {
                Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                mContext.startActivity(launchIntent);

                Intent intent = new Intent("android.intent.action.MAIN");
                intent.setComponent(ComponentName.unflattenFromString("com.ihelp101.dontswipe/com.ihelp101.dontswipe.Respect"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        } catch (Exception e) {

        }
    }
}


