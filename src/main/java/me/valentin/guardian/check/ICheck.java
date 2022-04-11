package me.valentin.guardian.check;

public interface ICheck<T> {

	Class<T> getType();

	void handle(T type);

}
