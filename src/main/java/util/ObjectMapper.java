package util;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;



public class ObjectMapper {

	private static ModelMapper modelMapper = new ModelMapper();


	static {
		modelMapper = new ModelMapper();
	}

	private ObjectMapper() {
	}

	public static <D, T> D map(final T entity, Class<D> outClass) {
	        if(entity == null){
	           return null;
	        }
		return modelMapper.map(entity, outClass);
	}
	public static <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outCLass) {
		return entityList.stream().map(entity -> map(entity, outCLass)).collect(Collectors.toList());
	}

	public static <S, D> D map(final S source, D destination) {
		modelMapper.map(source, destination);
		return destination;
	}
}