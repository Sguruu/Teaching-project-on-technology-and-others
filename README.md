# RxTraining (Version 1.0)
Проект, предназначенный для прокачки скиллов по RxJava2.
Представляет из себя набор методов, покрытых тестами.
Необходимо реализовать тело методов так, чтобы тесты проходили.
Как правило каждый метод заточен на изучение и применение конкретного оператора.

Последовательность выполнения задач:
- RxCreating
- RxTransforming
- RxCombining
- RxFiltering
- RxErrors
- RxSingle
- RxCompletable
- RxMaybe

[Быстрый переход к коду проекта](https://github.com/Sguruu/Teaching-project-on-technology-and-others/tree/RxJava_training/app/src/main/java/ru/artkorchagin/rxtraining)

[Быстрый переход к тестам проекта](https://github.com/Sguruu/Teaching-project-on-technology-and-others/tree/RxJava_training/app/src/test/java/ru/artkorchagin/rxtraining/rx)

## Использованные источники:
1. [Документация ReactiveX](http://reactivex.io/)
2. [Interactive diagrams of Rx](https://rxmarbles.com/#filter)
3. [Статья: Справочник по источникам событий в Rx](https://habr.com/ru/company/funcorp/blog/459174/)
##

# Важные моменты 

### RxCreating
```.java
package ru.artkorchagin.rxtraining.rx;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableRange;
import io.reactivex.subjects.PublishSubject;
import ru.artkorchagin.rxtraining.exceptions.ExpectedException;
import ru.artkorchagin.rxtraining.exceptions.NotImplementedException;

/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 13.11.18
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class RxCreatingTraining {

    /* Тренировочные методы */

    /**
     * Эммит одного элемента
     *
     * @param value - Произвольное число
     * @return {@link Observable}, который эммитит только значение {@code value}
     */
    public Observable<Integer> valueToObservable(int value) {
        return Observable.just(value);
    }

    /**
     * Эммит элементов массива в {@link Observable}
     *
     * @param array - Массив произвольных строк
     * @return {@link Observable}, который эммитит по порядку все строки из заданного массива
     */
    public Observable<String> arrayToObservable(String[] array) {
        return Observable.fromArray(array);
    }

    /**
     * Выполнение метода с длительными вычислениями: {@link #expensiveMethod()}. Необходимо, чтобы метод
     * вызывался только при подписке на Observable
     *
     * @return {@link Observable} - который эммитит результат выполнения метода
     * {@link #expensiveMethod()}
     */
    public Observable<Integer> expensiveMethodResult() {

        return Observable.create(emmiter->{
            emmiter.onNext(expensiveMethod());
            emmiter.onComplete();
        });
    }

    /**
     * Возрастающая последовательность, начинающаяся с нуля с первоначальной задержкой и заданным
     * интервалом
     *
     * @return {@link Observable} - который эммитит возрастающую последовательность значений,
     * начиная с 0L, пока не произойдёт отписка.
     * Значения начинают эммититься с задержкой {@code initialDelay} миллисекунд и каждый
     * последующий с интервалом {@code period} миллисекунд.
     * {@code onError} или {@code onComplete} не должны вызваться.
     */
    public Observable<Long> increasingSequenceWithDelays(long initialDelay, long period) {
        return Observable.interval(initialDelay,period, TimeUnit.MILLISECONDS);
    }

    /**
     * Возращение значения 0L с заданной задержкой
     *
     * @param delay - Задержка
     * @return Observable который эммитит только одно значение 0L с указанной
     * задержкой {@code delay}
     */
    public Observable<Long> delayedZero(long delay) {
        return Observable.timer(delay,TimeUnit.MILLISECONDS);
    }

    /**
     * Последовательный вызов нескольких методов с длительными вычислениями.
     *
     * @param unstableCondition - условие, которое необходимо передавать в {@code unstableMethod}
     * @return {@link Observable} который последовательно эммитит результаты выполнения методов, в
     * следующем порядке:
     * 1. {@link #expensiveMethod()}
     * 2. {@link #alternativeExpensiveMethod()}
     * 3. {@link #unstableMethod(boolean)}
     */
    public Observable<Integer> combinationExpensiveMethods(final boolean unstableCondition) {
        PublishSubject<Integer> subject = PublishSubject.create();
        subject.subscribe();
        subject.onNext(expensiveMethod());
        subject.onNext(alternativeExpensiveMethod());
        subject.onNext(unstableMethod(unstableCondition));
        subject.subscribe();
        return subject;
    }

    /**
     * Без каких либо событий
     *
     * @return {@link Observable} который не эммитит ни одного элемента и не вызывает
     * {@code onComplete} или {@code onError}
     */
    public Observable<Integer> withoutAnyEvents() {
        return Observable.never();
    }

    /**
     * Пустая последовательность
     *
     * @return {@link Observable} который не эммитит значения, вызывается только {@code onComplete}
     */
    public Observable<Integer> onlyComplete() {

        return Observable.empty();
    }

    /**
     * Только одна ошибка
     *
     * @return {@link Observable} который не эммитит значения, только в {@code onError} приходит
     * ошибка {@link ExpectedException}
     */
    public Observable<Integer> onlyError() {
        return Observable.error(new ExpectedException());
    }

    /* Вспомогательные методы */

    /**
     * Длительные вычисления. (Вспомогательный метод! Не изменять!)
     *
     * @return Результат вычислений
     */
    int expensiveMethod() {
        // Some Expensive Calculations
        return Integer.MAX_VALUE;
    }

    /**
     * Длительные вычисления. (Вспомогательный метод! Не изменять!)
     *
     * @return Результат вычислений
     */
    int alternativeExpensiveMethod() {
        // Some Expensive Calculations
        return Integer.MAX_VALUE;
    }

    /**
     * Метод, генерирующий ошибку при unstableCondition=true
     * (Вспомогательный метод! Не изменять!)
     *
     * @return Результат вычислений
     */
    int unstableMethod(boolean unstableCondition) {
        if (unstableCondition) {
            throw new ExpectedException();
        }
        return Integer.MAX_VALUE;
    }

}


```
### RxTransforming
```.java
package ru.artkorchagin.rxtraining.rx;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observables.GroupedObservable;
import ru.artkorchagin.rxtraining.entity.Entity;
import ru.artkorchagin.rxtraining.exceptions.NotImplementedException;

/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 13.11.18
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class RxTransformingTraining {

    /* Тренировочные методы */

    /**
     * Преобразование чисел в строки
     *
     * @param intObservable - источник
     * @return {@link Observable<String>} - который эммитит строки,
     * преобразованные из чисел в {@code intObservable}
     */
    public Observable<String> transformIntToString(Observable<Integer> intObservable) {
        return intObservable.map(String::valueOf);
    }

    /**
     * Преобразование {@link Observable<Integer>} эммитящих идентификаторы сущностей в сами
     * сущности, которые должны быть получены с помощью метода {@link #requestApiEntity(int)}
     *
     * @param idObservable - идентификаторы сущностей
     * @return {@link Observable<Entity>} эммитит сущности, соответствующие идентификаторам из
     * {@code idObservable}
     */
    public Observable<Entity> requestEntityById(Observable<Integer> idObservable) {
        return idObservable.flatMap(this::requestApiEntity);
    }

    /**
     * Распределение имён из {@code namesObservable} по первой букве имени, в отдельные
     * {@link GroupedObservable}
     *
     * @param namesObservable - {@link Observable<String>} с именами
     * @return {@link Observable} который эммитит {@link GroupedObservable} - сгруппированный
     * поток имён объединённых первой буквой в имени
     */
    public Observable<GroupedObservable<Character, String>> distributeNamesByFirstLetter(Observable<String> namesObservable) {
        return namesObservable.groupBy(name -> name.charAt(0));
    }

    /**
     * Объединить элементы, полученные из {@code intObservable} в списки {@link List} с максимальным
     * размером {@code listsSize}
     *
     * @param listsSize     максимальный размер списка элементов
     * @param intObservable {@link Observable} с произвольным количеством рандомных чисел
     * @return {@code Observable} который эммитит списки чисел из {@code intObservable}
     */
    public Observable<List<Integer>> collectsIntsToLists(int listsSize, Observable<Integer> intObservable) {
        return intObservable.buffer(listsSize);
    }

    /* Вспомогательные методы */

    /**
     * Выполнение HTTP запроса и эммит полученной сущности, соответствующей заданному идентификатору
     * (Вспомогательный метод! Не изменять!)
     *
     * @param id - Идентификатор сущности {@link Entity}
     * @return {@link Observable<Entity>} который эммитит полученную сущность
     */
    Observable<Entity> requestApiEntity(int id) {
        // Выполнение запроса и эммит сущности
        return Observable.just(new Entity(id));
    }

}

```
### RxCombining
```.java
package ru.artkorchagin.rxtraining.rx;


import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import ru.artkorchagin.rxtraining.exceptions.NotImplementedException;

/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 15.11.18
 */
public class RxCombiningTraining {

    /* Тренировочные методы */

    /**
     * Суммирование элементов двух последовательностей.
     *
     * @param integerObservable1 {@link Observable} с произвольным количеством рандомных чисел
     * @param integerObservable2 {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит числа, где i-й элемент равен сумме i-го элемента
     * {@code integerObservable1} и i-го элемента {@code integerObservable2}. Если в одной из
     * входящих последовательностей сработает  {@code onComplete или  {@code onError} то и в
     * результирующей последовательности тоже сработает этот метод.
     */
    public Observable<Integer> summation(Observable<Integer> integerObservable1, Observable<Integer> integerObservable2) {
        return Observable.zip(integerObservable1, integerObservable2, (value1, value2) -> value1 + value2);
    }

    /**
     * Поиск элементов по выбранной строке и категории
     *
     * @param searchObservable   Последовательность поисковых строк (в приложении может быть
     *                           введёнными строками в поисковую строку)
     * @param categoryObservable Последовательность категорий, которые необходимо отобразить
     * @return {@link Observable}  который эммитит списки элементов, с учётом поисковой строки из
     * {@code searchObservable} и выбранной категории из {@code categoryObservable}
     * @see #searchItems(String searchString, int categoryId)
     */
    public Observable<List<String>> requestItems(Observable<String> searchObservable,
                                                 Observable<Integer> categoryObservable) {
        return Observable.combineLatest(searchObservable, categoryObservable,
                (value1, value2) -> searchItems(value1, value2));
    }

    /**
     * Композиция потоков, обращение с несколькими объектами {@link Observable}, как с одним.
     *
     * @param intObservable1 {@link Observable} с произвольным количеством рандомных чисел
     * @param intObservable2 {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит элементы из {@code intObservable1} и
     * {@code intObservable2}
     */
    public Observable<Integer> composition(Observable<Integer> intObservable1,
                                           Observable<Integer> intObservable2) {
        return intObservable1.mergeWith(intObservable2);
    }

    /**
     * Дополнительный элемент перед всеми элементами потока
     *
     * @param firstItem     Первый элемент, который необходимо добавить
     * @param intObservable {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который сначала эммитит элемент {@code firstItem}, а потом все
     * элементы последовательности {@code intObservable}
     */
    public Observable<Integer> additionalFirstItem(int firstItem, Observable<Integer> intObservable) {
        return intObservable.startWith(firstItem);
    }

    /* Вспомогательные методы */

    /**
     * Поиск по строкам и вывод
     *
     * @param searchString Строка поиска
     * @param categoryId   Категория
     * @return вывод некоторых элементов с учётом поисковой строки и выбранной категории
     */
    List<String> searchItems(String searchString, int categoryId) {
        // Поиск и выборка
        return Collections.emptyList();
    }

}


```
### RxFiltering
```.java
package ru.artkorchagin.rxtraining.rx;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import ru.artkorchagin.rxtraining.exceptions.NotImplementedException;

/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 14.11.18
 */
public class RxFilteringTraining {

    /* Тренировочные методы */

    /**
     * Только положительные числа
     *
     * @param intValues {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} только с положительными числами, отрицательные должны быть
     * отфильтрованы
     */
    public Observable<Integer> onlyPositiveNumbers(Observable<Integer> intValues) {
        return intValues.filter(value1->value1>0);
    }

    /**
     * Эммит только последних значений
     *
     * @param count     Количество последних элементов, которые нужно эммитить
     * @param intValues {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит последние значения
     */
    public Observable<Integer> onlyLastValues(int count, Observable<Integer> intValues) {
        return intValues.takeLast(count);
    }

    /**
     * Эммит только первых значений
     *
     * @param count     Количество первых элементов, которые нужно эммитить
     * @param intValues {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит первые значения
     */
    public Observable<Integer> onlyFirstValues(int count, Observable<Integer> intValues) {
        return intValues.takeLast(count);
    }

    /**
     * Отфильтровать первые значения
     *
     * @param count     Количество первых элементов, которые нужно отфильтровать
     * @param intValues {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит значения из {@code intValues} кроме первых
     * {@code count} значений
     */
    public Observable<Integer> ignoreFirstValues(int count, Observable<Integer> intValues) {
        return intValues.skip(count);
    }

    /**
     * Только последний элемент из всех элементов во временном периоде
     *
     * @param periodMills Период в миллисекундах, за который необходимо произвести выборку
     *                    последнего элемента
     * @param intValues   {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит максимум 1 значения за интервал
     * {@code periodMills}
     */
    public Observable<Integer> onlyLastPerInterval(int periodMills, Observable<Integer> intValues) {
        return intValues.sample(periodMills, TimeUnit.MILLISECONDS);
    }

    /**
     * Ошибка при длительном ожидании элементов
     *
     * @param timeMills Время ожидания в миллисекундах
     * @param intValues {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит значения {@code intValues}, или выдаёт ошибку,
     * если время ожидания превышает {@code timeMills}
     */
    public Observable<Integer> errorIfLongWait(int timeMills, Observable<Integer> intValues) {
        return intValues.timeout(timeMills,TimeUnit.MILLISECONDS);
    }

    /**
     * Значения без повторений
     *
     * @param intValues {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит значения {@code intValues}, но без повторяющихся
     * значений
     */
    public Observable<Integer> ignoreDuplicates(Observable<Integer> intValues) {
        return intValues.distinct();
    }

    /**
     * Игноритуются повторяющиеся элементы, которые идут подряд
     *
     * @param intValues {@link Observable} с произвольным количеством рандомных чисел
     * @return {@link Observable} который эммитит значения {@code intValues}, но если новое значение
     * повторяет предыдущее, оно пропускается
     */
    public Observable<Integer> onlyChangedValues(Observable<Integer> intValues) {
        return intValues.distinctUntilChanged();
    }

}

```
### RxErrors
```.java
package ru.artkorchagin.rxtraining.rx;

import io.reactivex.Observable;
import ru.artkorchagin.rxtraining.exceptions.NotImplementedException;

/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 20.11.18
 */
public class RxErrorsTraining {

    /* Тренировочные методы */

    /**
     * В случае ошибки передавать значение по умолчанию
     *
     * @param intObservable {@link Observable} с произвольным количеством рандомных чисел, который
     *                      может передавать ошибку
     * @param defaultValue  значение по умолчанию, передавать в случае, если последовательность
     *                      {@code intObservable} завершилась с ошибкой {@link Throwable}
     * @return {@link Observable} который эммитит значения из {@code intObservable}, либо
     * defaultValue
     */
    Observable<Integer> handleErrorsWithDefaultValue(Observable<Integer> intObservable, final Integer defaultValue) {
     return    intObservable.onErrorReturnItem(defaultValue);
    }

    /**
     * В случае ошибки переключаться на другую последовательность
     *
     * @param intObservable      {@link Observable} с произвольным количеством рандомных чисел, который
     *                           может передавать ошибку
     * @param fallbackObservable {@link Observable} последовательность, на которую нужно
     *                           переключиться в случае ошибки
     * @return {@link Observable} который эммитит значения из {@code intObservable}, либо
     * {@code fallbackObservable}
     */
    Observable<Integer> handleErrorsWithFallbackObservable(Observable<Integer> intObservable, Observable<Integer> fallbackObservable) {
        return intObservable.onErrorResumeNext(fallbackObservable);
    }
}


```
### RxSingle
```.java
package ru.artkorchagin.rxtraining.rx;

import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.artkorchagin.rxtraining.exceptions.ExpectedException;
import ru.artkorchagin.rxtraining.exceptions.NotImplementedException;

/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 20.11.18
 */
public class RxSingleTraining {

    /* Тренировочные методы */

    /**
     * Эммит только 1 положительного элемента либо ошибка {@link ExpectedException}
     *
     * @param value любое произвольное число
     * @return {@code Single} который эммитит значение {@code value} если оно положительное,
     * либо ошибку {@link ExpectedException} если оно отрицательное
     */
    Single<Integer> onlyOneElement(Integer value) {
        return Single.create(emmiter -> {
                    if (value > 0) {
                        emmiter.onSuccess(value);
                    } else {
                        emmiter.onError(new ExpectedException());
                    }
                }
        );
    }

    /**
     * Преобразование последовательности {@code Observable} в {@code Single}
     *
     * @param integerObservable {@link Observable} произвольная последовательность чисел
     * @return {@link Single} который эммитит либо самый первый элемент последовательности
     * {@code integerObservable}, либо ошибку {@link NoSuchElementException} в случае, если
     * последовательность пустая
     */
    Single<Integer> onlyOneElementOfSequence(Observable<Integer> integerObservable) {
       return integerObservable.firstOrError();
    }

    /**
     * Сумма всех элементов последовательности
     *
     * @param integerObservable {@link Observable} произвольная последовательность чисел
     * @return {@link Single} который эммитит сумму всех элементов, либо 0 если последовательность
     * пустая
     */
    Single<Integer> calculateSumOfValues(Observable<Integer> integerObservable) {
       return integerObservable.reduce((value1,value2)->value1+value2).toSingle(0);
    }

    /**
     * Преобразование последовательности в список
     *
     * @param integerObservable {@link Observable} произвольная последовательность чисел
     * @return {@link Single} который эммитит {@link List} со всеми элементами последовательности
     * {@code integerObservable}
     */
    Single<List<Integer>> collectionOfValues(Observable<Integer> integerObservable) {
        return integerObservable.toList();
    }

    /**
     * Проверка всех элементов на положительность
     *
     * @param integerSingle {@link Observable} произвольная последовательность чисел
     * @return {@link Single} который эммитит {@code true} если все элементы последовательности
     * {@code integerSingle} положительны, {@code false} если есть отрицательные элементы
     */
    Single<Boolean> allElementsIsPositive(Observable<Integer> integerSingle) {
        return integerSingle.all((value1)->value1>0);
    }

}


```
### RxCompletable
```.java
package ru.artkorchagin.rxtraining.rx;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Single;
import ru.artkorchagin.rxtraining.exceptions.ExpectedException;
import ru.artkorchagin.rxtraining.exceptions.NotImplementedException;

/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 20.11.18
 */
public class RxCompletableTraining {

    /* Тренировочные методы */

    /**
     * Выполнение метода {@link #havyMethod()} внутри {@link Completable} и вызов {@code onComplete}
     *
     * @return {@link Completable}, который вызывает {@link #havyMethod()}
     */
    Completable callFunction() {
        return Completable.fromAction(this::havyMethod);
    }

    /**
     * Завершить последовательность, если {@code checkSingle} эммитит {@code true} или эммитит
     * ошибку, если {@code checkSingle} эммитит {@code false}
     *
     * @param checkSingle @{link Single} который эммитит {@code true} или {@code false}
     * @return {@code Completable}
     */
    Completable completeWhenTrue(Single<Boolean> checkSingle) {
        return checkSingle.flatMapCompletable(value->{
            if(value){
                return Completable.complete();
            } else {
                return Completable.error(new ExpectedException());
            }
        });
    }

    /* Вспомогательные методы */

    /**
     * Тяжёлый метод
     * (Вспомогательный метод! Не изменять!)
     */
    void havyMethod() {
        // Выполнение операций
    }

}


```
### RxMaybe
```.java
package ru.artkorchagin.rxtraining.rx;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.artkorchagin.rxtraining.exceptions.NotImplementedException;

/**
 * @author Arthur Korchagin (artur.korchagin@simbirsoft.com)
 * @since 20.11.18
 */
public class RxMaybeTraining {

    /* Тренировочные методы */

    /**
     * Эммит только 1 положительного элемента либо пустая последовательность
     *
     * @param value любое произвольное число
     * @return {@code Maybe} который эммитит значение {@code value} если оно положительное,
     * либо не эммитит ничего, если {@code value} отрицательное
     */
    Maybe<Integer> positiveOrEmpty(Integer value) {
        return Maybe.create(emmiter -> {
            if (value > 0) {
                emmiter.onSuccess(value);
            } else {
                emmiter.onComplete();
            }
        });
    }

    /**
     * Эммит только 1 положительного элемента либо пустая последовательность
     *
     * @param valueSingle {@link Single} который эммитит любое произвольное число
     * @return {@code Maybe} который эммитит значение из {@code valueSingle} если оно эммитит
     * положительное число, иначе не эммитит ничего
     */
    Maybe<Integer> positiveOrEmpty(Single<Integer> valueSingle) {
        return valueSingle.filter(value1 -> value1 > 0);
    }

    /**
     * Сумма всех элементов последовательности
     *
     * @param integerObservable {@link Observable} произвольная последовательность чисел
     * @return {@link Maybe} который эммитит сумму всех элементов, либо не эммитит ничего если
     * последовательность пустая
     */
    Maybe<Integer> calculateSumOfValues(Observable<Integer> integerObservable) {
        return integerObservable.reduce((value1,value2)->value1+value2);
    }

    /**
     * Если {@code integerMaybe} не эммитит элемент, то возвращать {@code defaultValue}
     *
     * @param defaultValue произвольное число
     * @return {@link Single} который эммитит значение из {@code integerMaybe}, либо
     * {@code defaultValue} если последовательность пустая
     */
    Single<Integer> leastOneElement(Maybe<Integer> integerMaybe, int defaultValue) {
        return integerMaybe.defaultIfEmpty(defaultValue).toSingle();
    }

}


```
# Стадия готовности проекта : ГОТОВ 
