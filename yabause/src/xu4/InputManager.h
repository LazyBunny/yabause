#ifndef _INPUTMANAGER_H_
#define _INPUTMANAGER_H_

#include <SDL.h>
#include <vector>
#include <map>
#include <string>

class InputConfig;
class YWindow;

class MenuScreen;

//you should only ever instantiate one of these, by the way
class InputManager
{
private:
	InputManager();

	static InputManager* mInstance;

	static const int DEADZONE = 23000;

	void loadDefaultKBConfig();

	std::map<SDL_JoystickID, SDL_Joystick*> mJoysticks;
	std::map<SDL_JoystickID, InputConfig*> mInputConfigs;
	InputConfig* mKeyboardInputConfig;

	std::map<SDL_JoystickID, int*> mPrevAxisValues;
	std::map<SDL_JoystickID, uint8_t*> mHatValues;

	bool initialized() const;

	void addJoystickByDeviceIndex(int id);
	void removeJoystickByJoystickID(SDL_JoystickID id);
	bool loadInputConfig(InputConfig* config); // returns true if successfully loaded, false if not (or didn't exist)
	MenuScreen * menu_layer_ = nullptr;

public:
	virtual ~InputManager();

	static InputManager* getInstance();

	void writeDeviceConfig(InputConfig* config);
	static std::string getConfigPath();

	void init();
	void deinit();

	int getNumJoysticks();
	int getButtonCountByDevice(int deviceId);
	int getNumConfiguredDevices();

	std::string getDeviceGUIDString(int deviceId);

	InputConfig* getInputConfigByDevice(int deviceId);

	int handleJoyEvents(void);
	bool parseEvent(const SDL_Event& ev );

	int handleJoyEventsMenu(void);
	bool parseEventMenu(const SDL_Event& ev );

	void setMenuLayer( MenuScreen * menu_layer ){
		menu_layer_ = menu_layer;
	}

	uint32_t select_button_ = 0;
	uint32_t showmenu_ = 0;
	void setToggleMenuEventCode( uint32_t type ){ showmenu_ = type; }

	void setGamePadomode( int user, int mode );
	
};

#endif