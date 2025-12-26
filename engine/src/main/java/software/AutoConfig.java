package software;


import hardware.engine.Engine;
import hardware.engine.rotorsManagers;
import hardware.parts.Reflector;
import hardware.parts.Rotor;

import java.util.ArrayList;
import java.util.List;

public class AutoConfig extends MachineConfig {

    public AutoConfig(StorageProvider SM) {
        super(SM);
    }

    private int getRandomIntInRange(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    private List<Character> generateRandomPositions(int size) {
        List<Character> positions = new ArrayList<>();
        String abc = storageManager.getABC();
        int abcLength = abc.length();

        for (int i = 0; i < size; i++) {
            int randomIndex = getRandomIntInRange(0, abcLength - 1);
            positions.add(abc.charAt(randomIndex));
        }
        return positions;
    }

    private List<Rotor> getRandomRotors(int count) {
        List<Rotor> selectedRotors = new ArrayList<>();
        List<Integer> usedIndices = new ArrayList<>();

        while (selectedRotors.size() < count) {
            int randomIndex = getRandomIntInRange(1, storageManager.getRotorsAmount());
            if (!usedIndices.contains(randomIndex)) {
                usedIndices.add(randomIndex);
                selectedRotors.add(storageManager.optionalGetRotorByID(randomIndex));
            }
        }
        return selectedRotors;
    }

    private Reflector getRandomReflector() {
        int reflectorId = getRandomIntInRange(1, storageManager.getReflectorsAmount());
        return storageManager.optionalGetReflectorByID(String.valueOf(reflectorId));
    }

    private String intToRome(int id) {
        return switch (id) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    @Override
    public Engine configureAndGetEngine() {
        int rotorsCount = 3; // You can change this value as needed
        List<Rotor> rotors = getRandomRotors(rotorsCount);
        List<Character> positions = generateRandomPositions(rotorsCount);
        storageManager.setOriginalPosition(positions);
        Reflector reflector = getRandomReflector();


        rotorsManagers manager = new rotorsManagers(rotors.toArray(new Rotor[0]));
        List<Integer> indexOfPositions = manager.MappingInputCharPositionByRightColumnToIndex(positions);
        manager.setRotorsPosition(indexOfPositions);
        return new Engine(reflector, manager, storageManager.getABC());
    }
}
