//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package aoc.kingdoms.lukasz.menusInGame;

import aoc.kingdoms.lukasz.events.Event;
import aoc.kingdoms.lukasz.events.EventOption;
import aoc.kingdoms.lukasz.events.EventsManager;
import aoc.kingdoms.lukasz.events.outcome.EventOutcome;
import aoc.kingdoms.lukasz.jakowski.CFG;
import aoc.kingdoms.lukasz.jakowski.Game;
import aoc.kingdoms.lukasz.jakowski.Missions.MissionTree;
import aoc.kingdoms.lukasz.jakowski.Renderer.Renderer;
import aoc.kingdoms.lukasz.jakowski.SoundsManager;
import aoc.kingdoms.lukasz.jakowski.Steam.SteamAchievementsManager;
import aoc.kingdoms.lukasz.map.ResourcesManager;
import aoc.kingdoms.lukasz.menu.Colors;
import aoc.kingdoms.lukasz.menu.Menu;
import aoc.kingdoms.lukasz.menu.MenuManager;
import aoc.kingdoms.lukasz.menu.menuTitle.MenuTitleIMG_DoubleText;
import aoc.kingdoms.lukasz.menu_element.Empty;
import aoc.kingdoms.lukasz.menu_element.MenuElement;
import aoc.kingdoms.lukasz.menu_element.Status;
import aoc.kingdoms.lukasz.menu_element.button.ButtonGame_Value;
import aoc.kingdoms.lukasz.menu_element.menuElementHover.MenuElement_Hover;
import aoc.kingdoms.lukasz.menu_element.menuElementHover.MenuElement_HoverElement;
import aoc.kingdoms.lukasz.menu_element.menuElementHover.MenuElement_HoverElement_Type;
import aoc.kingdoms.lukasz.menu_element.menuElementHover.MenuElement_HoverElement_Type_Image;
import aoc.kingdoms.lukasz.menu_element.menuElementHover.MenuElement_HoverElement_Type_Text;
import aoc.kingdoms.lukasz.menu_element.menuElementHover.MenuElement_HoverElement_Type_TextTitle_BG;
import aoc.kingdoms.lukasz.menu_element.textStatic.Text_Desc;
import aoc.kingdoms.lukasz.textures.ImageManager;
import aoc.kingdoms.lukasz.textures.Images;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lwjgl.opengl.GL20;
import team.rainfall.luminosity.annotations.Overlay;

import java.util.ArrayList;
import java.util.List;

public class InGame_Event extends Menu {
    protected static final int ANIMATION_TIME = 60;
    public static long lTime = 0L;
    public Event event;
    public static int eventType = 0;
    public static int eventID = 0;
    public int imgWidth = 1;
    public int imgHeight = 1;
    public boolean madeDecision = false;
    public InGame_Event(Event nEvent, int nEventType, int nEventID) {
        List<MenuElement> menuElements = new ArrayList();
        this.event = nEvent;
        eventType = nEventType;
        eventID = nEventID;
        if(event.super_event && event.musicName != null){
            try {
                Game.soundsManager.loadNextMusic(event.musicName);
            }catch (Exception e){
                CFG.exceptionStack(e);
            }
        }
        int paddingLeft = CFG.PADDING * 2 + Images.boxTitleBORDERWIDTH;
        int titleHeight = ImageManager.getImage(Images.title600).getHeight();
        int menuWidth = ImageManager.getImage(Images.title600).getWidth();
        int menuX = CFG.BUTTON_WIDTH + Renderer.boxBGExtraY + CFG.PADDING;
        int menuY = ImageManager.getImage(Images.topStats).getHeight() + CFG.PADDING * 2 + ImageManager.getImage(Images.title600).getHeight();
        int buttonY = CFG.PADDING * 2;
        int buttonX = Images.boxTitleBORDERWIDTH;
        EventsManager.loadEventIMG(this.event.image);

        try {
            float fScale = (float)(menuWidth - Images.boxTitleBORDERWIDTH * 2) / (float)EventsManager.eventIMG.getWidth();
            this.imgWidth = menuWidth - Images.boxTitleBORDERWIDTH * 2;
            this.imgHeight = (int)((float)EventsManager.eventIMG.getHeight() * fScale);
            buttonY += this.imgHeight;
        } catch (Exception var16) {
            Exception ex = var16;
            CFG.exceptionStack(ex);
        }

        if (eventType != 2 && eventType != 5) {
            menuElements.add(new Text_Desc(Game.lang.get(this.event.desc), paddingLeft, buttonY, menuWidth - paddingLeft * 2));
        } else {
            String sResource = "";
            String sPriceChange = "";

            try {
                sResource = ResourcesManager.getResourceName(((EventOutcome)((EventOption)this.event.options.get(0)).outcome.get(0)).getValue1());
                sPriceChange = CFG.getPrecision2(((EventOutcome)((EventOption)this.event.options.get(0)).outcome.get(0)).getValue2(), 10);
            } catch (Exception var15) {
                Exception ex = var15;
                CFG.exceptionStack(ex);
            }
            if(!event.no_text) {
                menuElements.add(new Text_Desc(Game.lang.get(this.event.desc, sResource, sPriceChange), paddingLeft, buttonY, menuWidth - paddingLeft * 2));
            }
        }

        buttonY += ((MenuElement)menuElements.get(menuElements.size() - 1)).getHeight() + CFG.PADDING * 2;

        int tMenuHeight;
        for(tMenuHeight = 0; tMenuHeight < this.event.options.size(); ++tMenuHeight) {
            menuElements.add(new ButtonGame_Value(Game.lang.get(((EventOption)this.event.options.get(tMenuHeight)).name), CFG.FONT_REGULAR, -1, paddingLeft, buttonY, menuWidth - paddingLeft * 2, true, tMenuHeight) {
                public void actionElement() {
                    Game.player.removeActiveEvent(InGame_Event.eventType, InGame_Event.eventID);
                    madeDecision = true;
                    if (InGame_Event.eventType == 999) {
                        MissionTree.takeMissionDecision(Game.player.iCivID, InGame_Event.eventID, this.getCurrent());
                    } else if (InGame_Event.eventType == 1000) {
                        MissionTree.takeMissionDecision_Civ(Game.player.iCivID, InGame_Event.eventID, this.getCurrent());
                        Game.player.currSituation.updateCurrentSituation();
                    } else {
                        EventsManager.takeEventDecision(Game.player.iCivID, InGame_Event.eventType, InGame_Event.eventID, this.getCurrent());
                        Game.player.currSituation.updateCurrentSituation();
                    }

                    Game.menuManager.rebuildInGame_Right();
                    Game.menuManager.setVisibleInGame_Event(false);
                    SteamAchievementsManager.unlockAchievement(SteamAchievementsManager.EVENT_RES);
                }

                public void buildElementHover() {
                    List<MenuElement_HoverElement> nElements = new ArrayList();
                    List<MenuElement_HoverElement_Type> nData = new ArrayList();
                    nData.add(new MenuElement_HoverElement_Type_TextTitle_BG(this.getText(), CFG.FONT_BOLD, Colors.HOVER_GOLD));
                    nElements.add(new MenuElement_HoverElement(nData));
                    nData.clear();

                    try {
                        for(int i = 0; i < ((EventOption)InGame_Event.this.event.options.get(this.getCurrent())).outcome.size(); ++i) {
                            if (((EventOutcome)((EventOption)InGame_Event.this.event.options.get(this.getCurrent())).outcome.get(i)).getStringLeft() != null) {
                                nData.add(new MenuElement_HoverElement_Type_Text(((EventOutcome)((EventOption)InGame_Event.this.event.options.get(this.getCurrent())).outcome.get(i)).getStringLeft(), CFG.FONT_REGULAR_SMALL));
                                if (((EventOutcome)((EventOption)InGame_Event.this.event.options.get(this.getCurrent())).outcome.get(i)).getStringRight() != null) {
                                    nData.add(new MenuElement_HoverElement_Type_Text(((EventOutcome)((EventOption)InGame_Event.this.event.options.get(this.getCurrent())).outcome.get(i)).getStringRight(), CFG.FONT_BOLD_SMALL, Colors.HOVER_GOLD));
                                }

                                if (((EventOutcome)((EventOption)InGame_Event.this.event.options.get(this.getCurrent())).outcome.get(i)).getImage() >= 0) {
                                    nData.add(new MenuElement_HoverElement_Type_Image(((EventOutcome)((EventOption)InGame_Event.this.event.options.get(this.getCurrent())).outcome.get(i)).getImage(), CFG.PADDING, ((EventOutcome)((EventOption)InGame_Event.this.event.options.get(this.getCurrent())).outcome.get(i)).getStringRight2(((EventOption)InGame_Event.this.event.options.get(this.getCurrent())).bonus_duration) != null ? CFG.PADDING : 0));
                                }

                                if (((EventOutcome)((EventOption)InGame_Event.this.event.options.get(this.getCurrent())).outcome.get(i)).getStringRight2(((EventOption)InGame_Event.this.event.options.get(this.getCurrent())).bonus_duration) != null) {
                                    nData.add(new MenuElement_HoverElement_Type_Text(((EventOutcome)((EventOption)InGame_Event.this.event.options.get(this.getCurrent())).outcome.get(i)).getStringRight2(((EventOption)InGame_Event.this.event.options.get(this.getCurrent())).bonus_duration), CFG.FONT_REGULAR_SMALL, Colors.HOVER_RIGHT2));
                                }

                                nElements.add(new MenuElement_HoverElement(nData));
                                nData.clear();
                            }
                        }
                    } catch (Exception var4) {
                        Exception ex = var4;
                        CFG.exceptionStack(ex);
                    }

                    this.menuElementHover = new MenuElement_Hover(nElements, nElements.size() == 1);
                }
            });
            buttonY += ((MenuElement)menuElements.get(menuElements.size() - 1)).getHeight() + CFG.PADDING;
        }

        buttonY = 0;
        tMenuHeight = 0;

        int inProvinceID;
        for(inProvinceID = menuElements.size(); tMenuHeight < inProvinceID; ++tMenuHeight) {
            if (buttonY < ((MenuElement)menuElements.get(tMenuHeight)).getPosY() + ((MenuElement)menuElements.get(tMenuHeight)).getHeight() + CFG.PADDING * 2) {
                buttonY = ((MenuElement)menuElements.get(tMenuHeight)).getPosY() + ((MenuElement)menuElements.get(tMenuHeight)).getHeight() + CFG.PADDING * 2;
            }
        }

        tMenuHeight = Math.min(buttonY, CFG.GAME_HEIGHT - menuY - CFG.PADDING * 2);
        menuElements.add(new Empty(0, 0, menuWidth, Math.max(buttonY, tMenuHeight)));
        inProvinceID = Game.getCiv(Game.player.iCivID).eventProvinceID;
        if (inProvinceID < 0 && Game.getCiv(Game.player.iCivID).getCapitalProvinceID() >= 0) {
            inProvinceID = Game.getCiv(Game.player.iCivID).getCapitalProvinceID();
        } else if (Game.getCiv(Game.player.iCivID).getNumOfProvinces() > 0) {
            inProvinceID = Game.getCiv(Game.player.iCivID).getProvinceID(Game.oR.nextInt(Game.getCiv(Game.player.iCivID).getNumOfProvinces()));
        }

        inProvinceID = Math.max(0, inProvinceID);
        if(event.execPosition >= 0){
            inProvinceID = event.execPosition;
        }
        this.initMenu(new MenuTitleIMG_DoubleText(Game.lang.get(this.event.title), Game.lang.get("EventInX", Game.getProvince(inProvinceID).getProvinceName()), true, false, Images.title600) {
            public long getTime() {
                return InGame_Event.lTime;
            }
        }, CFG.GAME_WIDTH / 2 - menuWidth / 2, CFG.GAME_HEIGHT / 5, menuWidth, tMenuHeight, menuElements, false, true);
        this.drawScrollPositionAlways = false;
    }

    public void draw(SpriteBatch oSB, int iTranslateX, int iTranslateY, boolean menuIsActive, Status titleStatus) {

        if(event.important){
            Game.gameThread.play = false;
        }

        if (lTime + 60L >= CFG.currentTimeMillis) {
            iTranslateY = iTranslateY - CFG.BUTTON_HEIGHT + (int)((float)CFG.BUTTON_HEIGHT * ((float)(CFG.currentTimeMillis - lTime) / 60.0F));
        }
        if(!event.no_background) {
            Renderer.drawBoxCorner(oSB, this.getPosX() + iTranslateX, this.getPosY() - this.getTitle().getHeight() + iTranslateY, this.getWidth(), this.getHeight() + this.getTitle().getHeight() + CFG.PADDING);
            Renderer.drawMenusBox(oSB, this.getPosX() + iTranslateX, this.getPosY() + iTranslateY, this.getWidth(), this.getHeight() + CFG.PADDING, false, Images.insideTop600, Images.insideBot600);
        }
        oSB.setColor(Color.WHITE);

        try {
            EventsManager.eventIMG.draw(oSB, this.getPosX() + Images.boxTitleBORDERWIDTH + iTranslateX, this.getPosY() + iTranslateY, this.imgWidth, this.imgHeight);
            if(!event.no_background) {
                Renderer.drawBox(oSB, Images.eventCorner, this.getPosX() + Images.boxTitleBORDERWIDTH + iTranslateX, this.getPosY() + iTranslateY, this.imgWidth, this.imgHeight, 1.0F);
                oSB.setColor(new Color(0.0F, 0.0F, 0.0F, 0.5F));
                ImageManager.getImage(Images.gradientVertical).draw(oSB, this.getPosX() + Images.boxTitleBORDERWIDTH + iTranslateX, this.getPosY() + this.imgHeight + iTranslateY, this.imgWidth, CFG.PADDING * 2);
                Images.gradientXY.draw(oSB, this.getPosX() + Images.boxTitleBORDERWIDTH + iTranslateX, this.getPosY() + this.imgHeight + iTranslateY, this.imgWidth, CFG.PADDING * 2, false, true);
            }
            oSB.setColor(Color.WHITE);
        } catch (Exception var7) {
        }
        super.draw(oSB, iTranslateX, iTranslateY, menuIsActive, titleStatus);
    }

    public void setVisible(boolean visible) {
        if(!madeDecision && !visible && event.important){
            Game.menuManager.addToast_Error("NotAllowedToCloseBeforeDecide");
            return;
        }
        super.setVisible(visible);
        lTime = CFG.currentTimeMillis;
    }
}
