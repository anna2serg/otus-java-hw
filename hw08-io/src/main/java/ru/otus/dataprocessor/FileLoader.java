package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;
import ru.otus.model.MeasurementProxy;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class FileLoader implements Loader {

    private final String fileName;
    private final ObjectMapper mapper;

    public FileLoader(String fileName) {
        this.fileName = fileName;
        this.mapper = new ObjectMapper();
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)){
            List<MeasurementProxy> measurements = mapper.readValue(in, new TypeReference<List<MeasurementProxy>>(){});
            return measurements.stream().map(MeasurementProxy::getMeasurement).collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
