#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test010 {


	struct __A;
	struct __A_VT;
	struct __B1;
	struct __B1_VT;
	struct __B2;
	struct __B2_VT;
	struct __C;
	struct __C_VT;

	typedef __A* A;
	typedef __B1* B1;
	typedef __B2* B2;
	typedef __C* C;

	struct __A
	{

	String a;

	__A_VT* __vptr;

	__A();

	static Class __class();

	static int32_t hashCode(A);

	static __A_VT __vtable;

	static void setA(A,String);
	static void printOther(A);
	static String toString(A);
	static String toString(A);

	};

	struct __A_VT
	{

	Class __isa;
	void (*setA)(A,String);
	void (*printOther)(A);
	String (*toString)(A);
	int32_t (*hashCode)(A);
	Class (*getClass)(A);
	bool (*equals)(A,Object);
	String (*toString)(A);

	__A_VT()
	: __isa(__A::__class()),
		setA(&__A::setA),
		printOther(&__A::printOther),
		toString(&__A::toString),
		hashCode(&__A::hashCode),
		getClass((Class(*)(A)) &__Object::getClass),
		equals((bool(*)(A,Object)) &__Object::equals)
		{}

	};

	struct __B1
	{

	String b;

	__B1_VT* __vptr;

	__B1();

	__A parent;

	static Class __class();

	static int32_t hashCode(B1);

	static __B1_VT __vtable;

	static String toString(B1);

	};

	struct __B1_VT
	{

	Class __isa;
	int32_t (*hashCode)(B1);
	Class (*getClass)(B1);
	bool (*equals)(B1,Object);
	String (*toString)(B1);

	__B1_VT()
	: __isa(__B1::__class()),
		hashCode(&__B1::hashCode),
		getClass((Class(*)(B1)) &__Object::getClass),
		equals((bool(*)(B1,Object)) &__Object::equals),
		toString(&__B1::toString)
		{}

	};

	struct __B2
	{

	String b;

	__B2_VT* __vptr;

	__B2();

	__A parent;

	static Class __class();

	static int32_t hashCode(B2);

	static __B2_VT __vtable;

	static String toString(B2);

	};

	struct __B2_VT
	{

	Class __isa;
	int32_t (*hashCode)(B2);
	Class (*getClass)(B2);
	bool (*equals)(B2,Object);
	String (*toString)(B2);

	__B2_VT()
	: __isa(__B2::__class()),
		hashCode(&__B2::hashCode),
		getClass((Class(*)(B2)) &__Object::getClass),
		equals((bool(*)(B2,Object)) &__Object::equals),
		toString(&__B2::toString)
		{}

	};

	struct __C
	{

	String c;

	__C_VT* __vptr;

	__C();

	__B1 parent;

	static Class __class();

	static int32_t hashCode(C);

	static __C_VT __vtable;

	static String toString(C);

	};

	struct __C_VT
	{

	Class __isa;
	int32_t (*hashCode)(C);
	Class (*getClass)(C);
	bool (*equals)(C,Object);
	String (*toString)(C);

	__C_VT()
	: __isa(__C::__class()),
		hashCode(&__C::hashCode),
		getClass((Class(*)(C)) &__Object::getClass),
		equals((bool(*)(C,Object)) &__Object::equals),
		toString(&__C::toString)
		{}

	};



	}
}

