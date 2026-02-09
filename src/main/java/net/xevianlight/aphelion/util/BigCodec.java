package net.xevianlight.aphelion.util;

import com.mojang.datafixers.util.*;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Since mojang only wanted to implement codecs up to 6 types, here's all the codecs 7-16. You're welcome.
 * @param <B>
 * @param <V>
 */
public interface BigCodec<B, V> extends StreamCodec<B, V> {

    // ---------- 7 ----------
    static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1, final Function<C, T1> g1,
            final StreamCodec<? super B, T2> c2, final Function<C, T2> g2,
            final StreamCodec<? super B, T3> c3, final Function<C, T3> g3,
            final StreamCodec<? super B, T4> c4, final Function<C, T4> g4,
            final StreamCodec<? super B, T5> c5, final Function<C, T5> g5,
            final StreamCodec<? super B, T6> c6, final Function<C, T6> g6,
            final StreamCodec<? super B, T7> c7, final Function<C, T7> g7,
            final Function7<T1, T2, T3, T4, T5, T6, T7, C> factory
    ) {
        return new StreamCodec<B, C>() {
            public @NotNull C decode(@NotNull B b) {
                T1 t1 = (T1)c1.decode(b);
                T2 t2 = (T2)c2.decode(b);
                T3 t3 = (T3)c3.decode(b);
                T4 t4 = (T4)c4.decode(b);
                T5 t5 = (T5)c5.decode(b);
                T6 t6 = (T6)c6.decode(b);
                T7 t7 = (T7)c7.decode(b);
                return (C)factory.apply(t1, t2, t3, t4, t5, t6, t7);
            }

            public void encode(@NotNull B b, @NotNull C v) {
                c1.encode(b, g1.apply(v));
                c2.encode(b, g2.apply(v));
                c3.encode(b, g3.apply(v));
                c4.encode(b, g4.apply(v));
                c5.encode(b, g5.apply(v));
                c6.encode(b, g6.apply(v));
                c7.encode(b, g7.apply(v));
            }
        };
    }

    // ---------- 8 ----------
    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1, final Function<C, T1> g1,
            final StreamCodec<? super B, T2> c2, final Function<C, T2> g2,
            final StreamCodec<? super B, T3> c3, final Function<C, T3> g3,
            final StreamCodec<? super B, T4> c4, final Function<C, T4> g4,
            final StreamCodec<? super B, T5> c5, final Function<C, T5> g5,
            final StreamCodec<? super B, T6> c6, final Function<C, T6> g6,
            final StreamCodec<? super B, T7> c7, final Function<C, T7> g7,
            final StreamCodec<? super B, T8> c8, final Function<C, T8> g8,
            final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> factory
    ) {
        return new StreamCodec<B, C>() {
            public @NotNull C decode(@NotNull B b) {
                T1 t1 = (T1)c1.decode(b);
                T2 t2 = (T2)c2.decode(b);
                T3 t3 = (T3)c3.decode(b);
                T4 t4 = (T4)c4.decode(b);
                T5 t5 = (T5)c5.decode(b);
                T6 t6 = (T6)c6.decode(b);
                T7 t7 = (T7)c7.decode(b);
                T8 t8 = (T8)c8.decode(b);
                return (C)factory.apply(t1, t2, t3, t4, t5, t6, t7, t8);
            }

            public void encode(@NotNull B b, @NotNull C v) {
                c1.encode(b, g1.apply(v));
                c2.encode(b, g2.apply(v));
                c3.encode(b, g3.apply(v));
                c4.encode(b, g4.apply(v));
                c5.encode(b, g5.apply(v));
                c6.encode(b, g6.apply(v));
                c7.encode(b, g7.apply(v));
                c8.encode(b, g8.apply(v));
            }
        };
    }

    // ---------- 9 ----------
    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1, final Function<C, T1> g1,
            final StreamCodec<? super B, T2> c2, final Function<C, T2> g2,
            final StreamCodec<? super B, T3> c3, final Function<C, T3> g3,
            final StreamCodec<? super B, T4> c4, final Function<C, T4> g4,
            final StreamCodec<? super B, T5> c5, final Function<C, T5> g5,
            final StreamCodec<? super B, T6> c6, final Function<C, T6> g6,
            final StreamCodec<? super B, T7> c7, final Function<C, T7> g7,
            final StreamCodec<? super B, T8> c8, final Function<C, T8> g8,
            final StreamCodec<? super B, T9> c9, final Function<C, T9> g9,
            Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C> factory
    ) {
        return new StreamCodec<>() {
            public @NotNull C decode(@NotNull B b) {
                T1 t1 = (T1)c1.decode(b);
                T2 t2 = (T2)c2.decode(b);
                T3 t3 = (T3)c3.decode(b);
                T4 t4 = (T4)c4.decode(b);
                T5 t5 = (T5)c5.decode(b);
                T6 t6 = (T6)c6.decode(b);
                T7 t7 = (T7)c7.decode(b);
                T8 t8 = (T8)c8.decode(b);
                T9 t9 = (T9)c9.decode(b);
                return (C)factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9);
            }

            public void encode(@NotNull B b, @NotNull C v) {
                c1.encode(b, g1.apply(v));
                c2.encode(b, g2.apply(v));
                c3.encode(b, g3.apply(v));
                c4.encode(b, g4.apply(v));
                c5.encode(b, g5.apply(v));
                c6.encode(b, g6.apply(v));
                c7.encode(b, g7.apply(v));
                c8.encode(b, g8.apply(v));
                c9.encode(b, g9.apply(v));
            }
        };
    }

    // ---------- 10 ----------
    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1, final Function<C, T1> g1,
            final StreamCodec<? super B, T2> c2, final Function<C, T2> g2,
            final StreamCodec<? super B, T3> c3, final Function<C, T3> g3,
            final StreamCodec<? super B, T4> c4, final Function<C, T4> g4,
            final StreamCodec<? super B, T5> c5, final Function<C, T5> g5,
            final StreamCodec<? super B, T6> c6, final Function<C, T6> g6,
            final StreamCodec<? super B, T7> c7, final Function<C, T7> g7,
            final StreamCodec<? super B, T8> c8, final Function<C, T8> g8,
            final StreamCodec<? super B, T9> c9, final Function<C, T9> g9,
            final StreamCodec<? super B, T10> c10, final Function<C, T10> g10,
            Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, C> factory
    ) {
        return new StreamCodec<>() {
            public @NotNull C decode(@NotNull B b) {
                T1 t1 = (T1)c1.decode(b);
                T2 t2 = (T2)c2.decode(b);
                T3 t3 = (T3)c3.decode(b);
                T4 t4 = (T4)c4.decode(b);
                T5 t5 = (T5)c5.decode(b);
                T6 t6 = (T6)c6.decode(b);
                T7 t7 = (T7)c7.decode(b);
                T8 t8 = (T8)c8.decode(b);
                T9 t9 = (T9)c9.decode(b);
                T10 t10 = (T10)c10.decode(b);
                return (C)factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
            }

            public void encode(@NotNull B b, @NotNull C v) {
                c1.encode(b, g1.apply(v));
                c2.encode(b, g2.apply(v));
                c3.encode(b, g3.apply(v));
                c4.encode(b, g4.apply(v));
                c5.encode(b, g5.apply(v));
                c6.encode(b, g6.apply(v));
                c7.encode(b, g7.apply(v));
                c8.encode(b, g8.apply(v));
                c9.encode(b, g9.apply(v));
                c10.encode(b, g10.apply(v));
            }
        };
    }

    // ---------- 11 ----------
    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1, final Function<C, T1> g1,
            final StreamCodec<? super B, T2> c2, final Function<C, T2> g2,
            final StreamCodec<? super B, T3> c3, final Function<C, T3> g3,
            final StreamCodec<? super B, T4> c4, final Function<C, T4> g4,
            final StreamCodec<? super B, T5> c5, final Function<C, T5> g5,
            final StreamCodec<? super B, T6> c6, final Function<C, T6> g6,
            final StreamCodec<? super B, T7> c7, final Function<C, T7> g7,
            final StreamCodec<? super B, T8> c8, final Function<C, T8> g8,
            final StreamCodec<? super B, T9> c9, final Function<C, T9> g9,
            final StreamCodec<? super B, T10> c10, final Function<C, T10> g10,
            final StreamCodec<? super B, T11> c11, final Function<C, T11> g11,
            Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, C> factory
    ) {
        return new StreamCodec<>() {
            public @NotNull C decode(@NotNull B b) {
                T1 t1 = (T1)c1.decode(b);
                T2 t2 = (T2)c2.decode(b);
                T3 t3 = (T3)c3.decode(b);
                T4 t4 = (T4)c4.decode(b);
                T5 t5 = (T5)c5.decode(b);
                T6 t6 = (T6)c6.decode(b);
                T7 t7 = (T7)c7.decode(b);
                T8 t8 = (T8)c8.decode(b);
                T9 t9 = (T9)c9.decode(b);
                T10 t10 = (T10)c10.decode(b);
                T11 t11 = (T11)c11.decode(b);
                return (C)factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
            }

            public void encode(@NotNull B b, @NotNull C v) {
                c1.encode(b, g1.apply(v));
                c2.encode(b, g2.apply(v));
                c3.encode(b, g3.apply(v));
                c4.encode(b, g4.apply(v));
                c5.encode(b, g5.apply(v));
                c6.encode(b, g6.apply(v));
                c7.encode(b, g7.apply(v));
                c8.encode(b, g8.apply(v));
                c9.encode(b, g9.apply(v));
                c10.encode(b, g10.apply(v));
                c11.encode(b, g11.apply(v));
            }
        };
    }

    // ---------- 12 ----------
    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1, final Function<C, T1> g1,
            final StreamCodec<? super B, T2> c2, final Function<C, T2> g2,
            final StreamCodec<? super B, T3> c3, final Function<C, T3> g3,
            final StreamCodec<? super B, T4> c4, final Function<C, T4> g4,
            final StreamCodec<? super B, T5> c5, final Function<C, T5> g5,
            final StreamCodec<? super B, T6> c6, final Function<C, T6> g6,
            final StreamCodec<? super B, T7> c7, final Function<C, T7> g7,
            final StreamCodec<? super B, T8> c8, final Function<C, T8> g8,
            final StreamCodec<? super B, T9> c9, final Function<C, T9> g9,
            final StreamCodec<? super B, T10> c10, final Function<C, T10> g10,
            final StreamCodec<? super B, T11> c11, final Function<C, T11> g11,
            final StreamCodec<? super B, T12> c12, final Function<C, T12> g12,
            Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, C> factory
    ) {
        return new StreamCodec<>() {
            public @NotNull C decode(@NotNull B b) {
                T1 t1 = (T1)c1.decode(b);
                T2 t2 = (T2)c2.decode(b);
                T3 t3 = (T3)c3.decode(b);
                T4 t4 = (T4)c4.decode(b);
                T5 t5 = (T5)c5.decode(b);
                T6 t6 = (T6)c6.decode(b);
                T7 t7 = (T7)c7.decode(b);
                T8 t8 = (T8)c8.decode(b);
                T9 t9 = (T9)c9.decode(b);
                T10 t10 = (T10)c10.decode(b);
                T11 t11 = (T11)c11.decode(b);
                T12 t12 = (T12)c12.decode(b);
                return (C)factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
            }

            public void encode(@NotNull B b, @NotNull C v) {
                c1.encode(b, g1.apply(v));
                c2.encode(b, g2.apply(v));
                c3.encode(b, g3.apply(v));
                c4.encode(b, g4.apply(v));
                c5.encode(b, g5.apply(v));
                c6.encode(b, g6.apply(v));
                c7.encode(b, g7.apply(v));
                c8.encode(b, g8.apply(v));
                c9.encode(b, g9.apply(v));
                c10.encode(b, g10.apply(v));
                c11.encode(b, g11.apply(v));
                c12.encode(b, g12.apply(v));
            }
        };
    }

    // ---------- 13 ----------
    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1, final Function<C, T1> g1,
            final StreamCodec<? super B, T2> c2, final Function<C, T2> g2,
            final StreamCodec<? super B, T3> c3, final Function<C, T3> g3,
            final StreamCodec<? super B, T4> c4, final Function<C, T4> g4,
            final StreamCodec<? super B, T5> c5, final Function<C, T5> g5,
            final StreamCodec<? super B, T6> c6, final Function<C, T6> g6,
            final StreamCodec<? super B, T7> c7, final Function<C, T7> g7,
            final StreamCodec<? super B, T8> c8, final Function<C, T8> g8,
            final StreamCodec<? super B, T9> c9, final Function<C, T9> g9,
            final StreamCodec<? super B, T10> c10, final Function<C, T10> g10,
            final StreamCodec<? super B, T11> c11, final Function<C, T11> g11,
            final StreamCodec<? super B, T12> c12, final Function<C, T12> g12,
            final StreamCodec<? super B, T13> c13, final Function<C, T13> g13,
            Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, C> factory
    ) {
        return new StreamCodec<>() {
            public @NotNull C decode(@NotNull B b) {
                T1 t1 = (T1)c1.decode(b);
                T2 t2 = (T2)c2.decode(b);
                T3 t3 = (T3)c3.decode(b);
                T4 t4 = (T4)c4.decode(b);
                T5 t5 = (T5)c5.decode(b);
                T6 t6 = (T6)c6.decode(b);
                T7 t7 = (T7)c7.decode(b);
                T8 t8 = (T8)c8.decode(b);
                T9 t9 = (T9)c9.decode(b);
                T10 t10 = (T10)c10.decode(b);
                T11 t11 = (T11)c11.decode(b);
                T12 t12 = (T12)c12.decode(b);
                T13 t13 = (T13)c13.decode(b);
                return (C)factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
            }

            public void encode(@NotNull B b, @NotNull C v) {
                c1.encode(b, g1.apply(v));
                c2.encode(b, g2.apply(v));
                c3.encode(b, g3.apply(v));
                c4.encode(b, g4.apply(v));
                c5.encode(b, g5.apply(v));
                c6.encode(b, g6.apply(v));
                c7.encode(b, g7.apply(v));
                c8.encode(b, g8.apply(v));
                c9.encode(b, g9.apply(v));
                c10.encode(b, g10.apply(v));
                c11.encode(b, g11.apply(v));
                c12.encode(b, g12.apply(v));
                c13.encode(b, g13.apply(v));
            }
        };
    }

    // ---------- 14 ----------
    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1, final Function<C, T1> g1,
            final StreamCodec<? super B, T2> c2, final Function<C, T2> g2,
            final StreamCodec<? super B, T3> c3, final Function<C, T3> g3,
            final StreamCodec<? super B, T4> c4, final Function<C, T4> g4,
            final StreamCodec<? super B, T5> c5, final Function<C, T5> g5,
            final StreamCodec<? super B, T6> c6, final Function<C, T6> g6,
            final StreamCodec<? super B, T7> c7, final Function<C, T7> g7,
            final StreamCodec<? super B, T8> c8, final Function<C, T8> g8,
            final StreamCodec<? super B, T9> c9, final Function<C, T9> g9,
            final StreamCodec<? super B, T10> c10, final Function<C, T10> g10,
            final StreamCodec<? super B, T11> c11, final Function<C, T11> g11,
            final StreamCodec<? super B, T12> c12, final Function<C, T12> g12,
            final StreamCodec<? super B, T13> c13, final Function<C, T13> g13,
            final StreamCodec<? super B, T14> c14, final Function<C, T14> g14,
            Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, C> factory
    ) {
        return new StreamCodec<>() {
            public @NotNull C decode(@NotNull B b) {
                T1 t1 = (T1)c1.decode(b);
                T2 t2 = (T2)c2.decode(b);
                T3 t3 = (T3)c3.decode(b);
                T4 t4 = (T4)c4.decode(b);
                T5 t5 = (T5)c5.decode(b);
                T6 t6 = (T6)c6.decode(b);
                T7 t7 = (T7)c7.decode(b);
                T8 t8 = (T8)c8.decode(b);
                T9 t9 = (T9)c9.decode(b);
                T10 t10 = (T10)c10.decode(b);
                T11 t11 = (T11)c11.decode(b);
                T12 t12 = (T12)c12.decode(b);
                T13 t13 = (T13)c13.decode(b);
                T14 t14 = (T14)c14.decode(b);
                return (C)factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
            }

            public void encode(@NotNull B b, @NotNull C v) {
                c1.encode(b, g1.apply(v));
                c2.encode(b, g2.apply(v));
                c3.encode(b, g3.apply(v));
                c4.encode(b, g4.apply(v));
                c5.encode(b, g5.apply(v));
                c6.encode(b, g6.apply(v));
                c7.encode(b, g7.apply(v));
                c8.encode(b, g8.apply(v));
                c9.encode(b, g9.apply(v));
                c10.encode(b, g10.apply(v));
                c11.encode(b, g11.apply(v));
                c12.encode(b, g12.apply(v));
                c13.encode(b, g13.apply(v));
                c14.encode(b, g14.apply(v));
            }
        };
    }

    // ---------- 15 ----------
    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1, final Function<C, T1> g1,
            final StreamCodec<? super B, T2> c2, final Function<C, T2> g2,
            final StreamCodec<? super B, T3> c3, final Function<C, T3> g3,
            final StreamCodec<? super B, T4> c4, final Function<C, T4> g4,
            final StreamCodec<? super B, T5> c5, final Function<C, T5> g5,
            final StreamCodec<? super B, T6> c6, final Function<C, T6> g6,
            final StreamCodec<? super B, T7> c7, final Function<C, T7> g7,
            final StreamCodec<? super B, T8> c8, final Function<C, T8> g8,
            final StreamCodec<? super B, T9> c9, final Function<C, T9> g9,
            final StreamCodec<? super B, T10> c10, final Function<C, T10> g10,
            final StreamCodec<? super B, T11> c11, final Function<C, T11> g11,
            final StreamCodec<? super B, T12> c12, final Function<C, T12> g12,
            final StreamCodec<? super B, T13> c13, final Function<C, T13> g13,
            final StreamCodec<? super B, T14> c14, final Function<C, T14> g14,
            final StreamCodec<? super B, T15> c15, final Function<C, T15> g15,
            Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, C> factory
    ) {
        return new StreamCodec<>() {
            public @NotNull C decode(@NotNull B b) {
                T1 t1 = (T1)c1.decode(b);
                T2 t2 = (T2)c2.decode(b);
                T3 t3 = (T3)c3.decode(b);
                T4 t4 = (T4)c4.decode(b);
                T5 t5 = (T5)c5.decode(b);
                T6 t6 = (T6)c6.decode(b);
                T7 t7 = (T7)c7.decode(b);
                T8 t8 = (T8)c8.decode(b);
                T9 t9 = (T9)c9.decode(b);
                T10 t10 = (T10)c10.decode(b);
                T11 t11 = (T11)c11.decode(b);
                T12 t12 = (T12)c12.decode(b);
                T13 t13 = (T13)c13.decode(b);
                T14 t14 = (T14)c14.decode(b);
                T15 t15 = (T15)c15.decode(b);
                return (C)factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
            }

            public void encode(@NotNull B b, @NotNull C v) {
                c1.encode(b, g1.apply(v));
                c2.encode(b, g2.apply(v));
                c3.encode(b, g3.apply(v));
                c4.encode(b, g4.apply(v));
                c5.encode(b, g5.apply(v));
                c6.encode(b, g6.apply(v));
                c7.encode(b, g7.apply(v));
                c8.encode(b, g8.apply(v));
                c9.encode(b, g9.apply(v));
                c10.encode(b, g10.apply(v));
                c11.encode(b, g11.apply(v));
                c12.encode(b, g12.apply(v));
                c13.encode(b, g13.apply(v));
                c14.encode(b, g14.apply(v));
                c15.encode(b, g15.apply(v));
            }
        };
    }

    // ---------- 16 ----------
    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> c1, final Function<C, T1> g1,
            final StreamCodec<? super B, T2> c2, final Function<C, T2> g2,
            final StreamCodec<? super B, T3> c3, final Function<C, T3> g3,
            final StreamCodec<? super B, T4> c4, final Function<C, T4> g4,
            final StreamCodec<? super B, T5> c5, final Function<C, T5> g5,
            final StreamCodec<? super B, T6> c6, final Function<C, T6> g6,
            final StreamCodec<? super B, T7> c7, final Function<C, T7> g7,
            final StreamCodec<? super B, T8> c8, final Function<C, T8> g8,
            final StreamCodec<? super B, T9> c9, final Function<C, T9> g9,
            final StreamCodec<? super B, T10> c10, final Function<C, T10> g10,
            final StreamCodec<? super B, T11> c11, final Function<C, T11> g11,
            final StreamCodec<? super B, T12> c12, final Function<C, T12> g12,
            final StreamCodec<? super B, T13> c13, final Function<C, T13> g13,
            final StreamCodec<? super B, T14> c14, final Function<C, T14> g14,
            final StreamCodec<? super B, T15> c15, final Function<C, T15> g15,
            final StreamCodec<? super B, T16> c16, final Function<C, T16> g16,
            Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, C> factory
    ) {
        return new StreamCodec<>() {
            public @NotNull C decode(@NotNull B b) {
                T1 t1 = (T1)c1.decode(b);
                T2 t2 = (T2)c2.decode(b);
                T3 t3 = (T3)c3.decode(b);
                T4 t4 = (T4)c4.decode(b);
                T5 t5 = (T5)c5.decode(b);
                T6 t6 = (T6)c6.decode(b);
                T7 t7 = (T7)c7.decode(b);
                T8 t8 = (T8)c8.decode(b);
                T9 t9 = (T9)c9.decode(b);
                T10 t10 = (T10)c10.decode(b);
                T11 t11 = (T11)c11.decode(b);
                T12 t12 = (T12)c12.decode(b);
                T13 t13 = (T13)c13.decode(b);
                T14 t14 = (T14)c14.decode(b);
                T15 t15 = (T15)c15.decode(b);
                T16 t16 = (T16)c16.decode(b);
                return (C)factory.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
            }

            public void encode(@NotNull B b, @NotNull C v) {
                c1.encode(b, g1.apply(v));
                c2.encode(b, g2.apply(v));
                c3.encode(b, g3.apply(v));
                c4.encode(b, g4.apply(v));
                c5.encode(b, g5.apply(v));
                c6.encode(b, g6.apply(v));
                c7.encode(b, g7.apply(v));
                c8.encode(b, g8.apply(v));
                c9.encode(b, g9.apply(v));
                c10.encode(b, g10.apply(v));
                c11.encode(b, g11.apply(v));
                c12.encode(b, g12.apply(v));
                c13.encode(b, g13.apply(v));
                c14.encode(b, g14.apply(v));
                c15.encode(b, g15.apply(v));
                c16.encode(b, g16.apply(v));
            }
        };
    }
}
