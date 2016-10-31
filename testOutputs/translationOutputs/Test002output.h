#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test002 {


	struct __A;
	struct __A_VT;

	typedef __A* A;

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



	}
}

