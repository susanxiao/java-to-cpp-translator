#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test005 {


	struct __A;
	struct __A_VT;
	struct __B;
	struct __B_VT;

	typedef __A* A;
	typedef __B* B;

	struct __A
	{

	__A_VT* __vptr;

	__A();

	static Class __class();

	static int32_t hashCode(A);

	static __A_VT __vtable;

	static String toString(A);

	};

	struct __A_VT
	{

	Class __isa;
	String (*toString)(A);
	int32_t (*hashCode)(A);
	Class (*getClass)(A);
	bool (*equals)(A,Object);

	__A_VT()
	: __isa(__A::__class()),
		toString(&__A::toString),
		hashCode(&__A::hashCode),
		getClass((Class(*)(A)) &__Object::getClass),
		equals((bool(*)(A,Object)) &__Object::equals)
		{}

	};

	struct __B
	{

	__B_VT* __vptr;

	__B();

	Class parent;

	static Class __class();

	static int32_t hashCode(B);

	static __B_VT __vtable;

	static String toString(B);

	};

	struct __B_VT
	{

	Class __isa;
	String (*toString)(B);
	int32_t (*hashCode)(B);
	Class (*getClass)(B);
	bool (*equals)(B,Object);

	__B_VT()
	: __isa(__B::__class()),
		toString(&__B::toString),
		hashCode(&__B::hashCode),
		getClass((Class(*)(B)) &__Object::getClass),
		equals((bool(*)(B,Object)) &__Object::equals)
		{}

	};



	}
}

