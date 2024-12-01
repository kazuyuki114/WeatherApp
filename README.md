# Weather App

A simple and user-friendly weather application that allows users to check the current weather based on their location or search for any city's weather. The app provides the option to switch between temperature units (Celsius and Fahrenheit), and stores the user's preference for future sessions.

## Features

- **Current Weather Information**: Get up-to-date weather information for your current location or any city you search for.
- **Temperature Units**: Switch between Celsius and Fahrenheit for temperature display.
- **Persistent Settings**: The app remembers your preferred temperature unit across sessions (Celsius or Fahrenheit).
- **User-Friendly UI**: Simple and intuitive interface for a smooth user experience.

## Technologies Used

- **Kotlin**: The app is developed using Kotlin, a modern programming language for Android development.
- **API**: The app fetches weather data using a weather API (e.g., OpenWeatherMap or any weather API of your choice).
- **Android SDK**: Android's core development platform for building the app.

## How to Use

### 1. Get Weather Information:
- Upon opening the app, it will show the current weather of your location (if location permission is granted).
- You can also search for any city by typing the city's name in the search bar.

### 2. Change Temperature Units:
- Use the toggle switch in the settings to switch between Celsius and Fahrenheit.
- The app will remember your selection and retain it even when you come back to the settings later.

### 3. Settings:
- In the settings screen, you can change the temperature unit (Celsius or Fahrenheit). The switch will reflect your current preference.

## Code Structure

- **MainActivity**: The main activity that displays the weather information and allows the user to search for cities.
- **SettingsActivity**: Activity where the user can toggle between Celsius and Fahrenheit.
- **WeatherDataManager**: A utility class for handling API requests and parsing weather data.
- **SharedData**: A singleton class for managing global app data such as the selected temperature unit.

## Future Enhancements

- **Hourly Forecast**: Add an hourly forecast feature to display the weather for the upcoming hours.
- **Weather Maps**: Integrate weather maps to show precipitation, temperature, and cloud coverage.
- **Push Notifications**: Notify users about severe weather conditions or daily weather summaries.
