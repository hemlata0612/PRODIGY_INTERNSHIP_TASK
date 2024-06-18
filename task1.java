import java.util.Scanner;

class TemperatureConverter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a temperature value: ");
        double temperature = scanner.nextDouble();

        System.out.print("Enter the original unit of measurement (Celsius, Fahrenheit, or Kelvin): ");
        String originalUnit = scanner.next().toLowerCase();

        double convertedToFahrenheit;
        double convertedToKelvin;

        switch (originalUnit) {
            case "celsius":
                convertedToFahrenheit = (temperature * 9 / 5) + 32;
                convertedToKelvin = temperature + 273.15;
                break;
            case "fahrenheit":
                convertedToFahrenheit = temperature;
                convertedToKelvin = (temperature + 459.67) * 5 / 9;
                break;
            case "kelvin":
                convertedToFahrenheit = (temperature * 9 / 5) - 459.67;
                convertedToKelvin = temperature;
                break;
            default:
                System.out.println("Invalid unit. Please enter Celsius, Fahrenheit, or Kelvin.");
                return;
        }

        System.out.println("Converted to Fahrenheit: " + convertedToFahrenheit);
        System.out.println("Converted to Kelvin: " + convertedToKelvin);
    }
}
